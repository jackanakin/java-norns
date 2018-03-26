package com.ark.norns.application;

import com.ark.norns.dataStructure.*;
import com.ark.norns.service.MibFileService;
import com.ark.norns.util.OidUtil;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MibManager {
    @Autowired
    private MibFileService mibFileService;

    @Autowired
    public MibManager(MibFileService mibFileService) {
        this.mibFileService = mibFileService;
    }

    // PROCESSO DE LEITURA DOS ARQUIVOS MIB
    // FAZ A LEITURA DO ARQUIVO .mib PARA ENCONTRAR OS OBJECT IDENTIFIER, ESTRUTURA PRINCIPAL e DEPENDÊNCIAS
    public TreeNode<Integer, String, MibFileOid> listMibFileObjectType(MibFile mibFile, TreeNode<Integer, String, MibFileOid> root) {
        BufferedReader br = null;
        FileReader fr = null;
        MibParsingStep step = MibParsingStep.findObjectType;

        String aux = null;
        List<String> auxList = null;
        String line = null;
        MibFileOid oid = null;
        Map<String, Set<Pair<MibFileOid, Integer>>> treeLeafs = new HashMap<>();

        try {
            //TODO
            fr = new FileReader(mibFile.getPwd());
            br = new BufferedReader(fr);

            READ_MODULE:
            try {
                while ((line = br.readLine()) != null) {
                    switch (step) {
                        case findObjectType:
                            aux = line.trim();
                            auxList = new ArrayList<>(Arrays.asList(aux.split(" ")));
                            auxList.removeAll(Arrays.asList("", " ", null));

                            if (aux.endsWith("OBJECT-TYPE") && auxList.size() == 2) {
                                aux = aux.split(" ")[0];
                                step = MibParsingStep.findObjectTypeSyntax;
                                oid = new MibFileOid(aux, mibFile);
                            }
                            break;
                        case findObjectTypeSyntax:
                            aux = line.trim();
                            auxList = new ArrayList<>(Arrays.asList(aux.split(" ")));
                            auxList.removeAll(Arrays.asList("", " ", null));

                            if (aux.startsWith("SYNTAX") && auxList.size() >= 2) {
                                if (line.contains("SEQUENCE OF")) {
                                    //TODO: mikrotik base loading
                                } else if (line.contains(oid.getIdentifier())) {
                                    step = MibParsingStep.findObjectTypeAccess; //TODO: validar
                                } else {
                                    for (String s : auxList) { //TODO: remove blank spaces before
                                        if (!s.equals("SYNTAX")) {
                                            MibParsingSyntax syntax = mibFile.getConventions().get(s);
                                            if (syntax == null) {
                                                try {
                                                    syntax = MibParsingSyntax.valueOf(s);
                                                    oid.setSyntax(syntax);
                                                    break;
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                oid.setSyntax(syntax);
                                                break;
                                            }
                                        }
                                    }
                                }
                                step = MibParsingStep.findObjectTypeAccess;
                            }
                            break;
                        case findObjectTypeAccess:
                            aux = line.trim();
                            auxList = new ArrayList<>(Arrays.asList(aux.split(" ")));
                            auxList.removeAll(Arrays.asList("", " ", null));

                            if ((aux.startsWith("ACCESS") || aux.startsWith("MAX-ACCESS")) && auxList.size() >= 2) {//TODO: fix
                                for (String s : auxList) {
                                    if (!s.contains("ACCESS")) {
                                        try {
                                            for (MibParsingAccess access : MibParsingAccess.values()) {
                                                if (access.getName().equals(s)) {
                                                    oid.setAccess(access);
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                                // PULEI ETAPA STATUS TODO: OBJECT-TYPE STATUS
                                step = MibParsingStep.findObjectTypeDescription;
                            }
                            break;
                        case findObjectTypeDescription:
                            aux = line.trim();
                            if (aux.startsWith("\"")) {
                                aux = aux.replaceFirst("\"", "");
                                if (aux.contains("\"")) {
                                    step = MibParsingStep.findObjectTypeIdentity;
                                    aux = aux.replace("\"", "");
                                } else {
                                    step = MibParsingStep.findingObjectTypeDescription;
                                }
                                oid.concatDescription(aux);
                            }
                            break;
                        case findingObjectTypeDescription:
                            aux = line.trim();
                            if (aux.contains("\"")) {
                                aux = aux.replace("\"", "");
                                step = MibParsingStep.findObjectTypeIdentity;
                            }
                            oid.concatDescription(aux);
                            break;
                        case findObjectTypeIdentity:
                            aux = line.trim();
                            if (aux.startsWith("::=")) {
                                String target = StringUtils.substringBetween(line, "{", "}");
                                String[] targetArray = target.trim().split(" ");
                                try {
                                    String parent = targetArray[0].trim();
                                    Integer value = Integer.parseInt(targetArray[1].trim());

                                    Set<Pair<MibFileOid, Integer>> leafs = treeLeafs.get(parent);
                                    if (leafs == null) {
                                        leafs = new HashSet<>();
                                    }
                                    leafs.add(new Pair<>(oid, value));
                                    treeLeafs.put(parent, leafs);
                                } catch (Exception e) {
                                }
                                step = MibParsingStep.findObjectType;
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); //TODO: handle exception - send MibFile, line ...
            }
            TreeNode<Integer, String, MibFileOid> mibFileNode = populateIanaTreeLeafs(treeLeafs, root.findParentNodeByOid(mibFile.getRootOID()));
            root.replaceNodeByOid(mibFile.getRootOID(), mibFileNode);
            return root;
        } catch (Exception e) {
            e.printStackTrace(); //TODO: handle exception - send MibFile, line ...
            return null;
        }
    }

    // CARREGA AS FOLHAS (OIDs finais) DOS MIBFILE NA ARVORE QUE É PASSADA
    public TreeNode<Integer, String, MibFileOid> populateIanaTreeLeafs(Map<String, Set<Pair<MibFileOid, Integer>>> treeLeafs,
                                                                       TreeNode<Integer, String, MibFileOid> root) {
        Map<String, Set<Pair<MibFileOid, Integer>>> aux = new HashMap<>();

        for (Map.Entry<String, Set<Pair<MibFileOid, Integer>>> entry : treeLeafs.entrySet()) {
            TreeNode<Integer, String, MibFileOid> parentNode = root.findParentNode(entry.getKey());
            if (parentNode != null) {
                for (Pair<MibFileOid, Integer> pair : entry.getValue()) {
                    String oidPath = parentNode.getPath() + "." + pair.getValue();

                    MibFileOid temp = pair.getKey();
                    temp.setOid(oidPath);
                    parentNode.addChildren(pair.getValue(), oidPath, temp);
                }
            } else {
                aux.put(entry.getKey(), entry.getValue());
            }
        }
        if (aux.size() > 0 && aux.size() != treeLeafs.size()) {
            root = populateIanaTreeLeafs(aux, root);
        }
        return root;
    }

    /// CRIAR ÁRVORE RAIZ -- TODA VEZ QUE É EXECUTADO O SNMP WALKER É CRIADO UMA NOVA RAIZ
    // É CHAMADO NO STARTUP DA APLICAÇÃO TAMBÉM
    public TreeNode<Integer, String, MibFileOid> initializeYggdrasil(TreeNode<Integer, String, MibFileOid> root) {
        String filePath = Properties.mandatoryMibFilesPath + Properties.mibFileRoot;
        MibFile yggdrasil = new MibFile(Properties.mibFileRoot, Properties.mandatoryMibFilesPath + Properties.mibFileRoot);
        try {
            Stream<String> fileStream = Files.lines(Paths.get(filePath));
            fileStream.forEach(line -> {
                TreeNode<Integer, String, MibFileOid> node = null;
                String path = null;
                for (String ch : OidUtil.pointDoublePointSplitter.split(line)) {
                    try {
                        if (node == null) {
                            node = root;
                            path = root.getPath();
                        } else {
                            path += "." + ch;
                            node = node.addChildren(Integer.parseInt(ch), path, new MibFileOid(path, null, yggdrasil));
                        }
                    } catch (Exception e) {
                        node.getData().setIdentifier(ch);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    public TreeNode<Integer, String, MibFileOid> createRootTreeNode() {
        TreeNode<Integer, String, MibFileOid> root = initializeYggdrasil(new TreeNode(1, "1", new MibFileOid("1", "iso", new MibFile(Properties.mibFileRoot, Properties.mandatoryMibFilesPath + Properties.mibFileRoot))));
        root = loadMandatoryMibFiles(root);
        return root;
    }

    public TreeNode<Integer, String, MibFileOid> loadMandatoryMibFiles(TreeNode<Integer, String, MibFileOid> root) {
        File folder = new File(Properties.mandatoryMibFilesPath);
        List<File> folderFiles = new ArrayList<>(Arrays.asList(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".mib") && pathname.isFile() && !name.equals(Properties.mibFileRoot);
            }
        })));

        Set<MibFile> mibFiles = new HashSet<MibFile>();

        MibFile auxMibFile = new MibFile();
        for (File file : folderFiles) {
            auxMibFile.setFileName(file.getName());
            if (!mibFiles.contains(auxMibFile) && !file.equals(Properties.mandatoryMibFilesPath)) {//verificar isso
                mibFiles.add(new MibFile(file.getName(), Properties.mandatoryMibFilesPath + file.getName()));
            }
        }
        mibFiles = loadMibIndexes(mibFiles, mibFiles, root, Properties.mandatoryMibFilesPath);
        root = populateIanaTreeMandatory(root, mibFiles, new HashSet<>());
        root.setMibFileComposition(mibFiles);
        return root;
    }

    private TreeNode<Integer, String, MibFileOid> populateIanaTreeMandatory(TreeNode<Integer, String, MibFileOid> root,
                                                                            Set<MibFile> toResolve, Set<MibFile> cached) {
        Set<MibFile> unresolved = new HashSet<>();

        LOOP:
        for (MibFile var : toResolve) {
            if (var.getDependencies().size() > 0) {
                Set<MibFile> intersect = new HashSet<>(cached);
                intersect.retainAll(var.getDependencies());
                if (intersect.size() != var.getDependencies().size()) {
                    unresolved.add(var);
                    continue LOOP;
                }
            }

            root = populateIanaTree(var.getModules(), var, root);
            root = listMibFileObjectType(var, root);
            cached.add(var);
        }
        if (unresolved.size() > 0 && unresolved.size() != toResolve.size()) {
            root = populateIanaTreeMandatory(root, unresolved, cached);
        }
        return root;
    }

    //INICIALIZAR INDICES DOS ARQUIVOS NA PASTA '/mib' exceto os obrigatórios para o BANCO DE DADOS
    public void createMibFilesIndexes() {
        TreeNode<Integer, String, MibFileOid> root = createRootTreeNode();

        List<String> persisted = mibFileService.getMibFileDAO().getMibFileRepository().findAllNames();
        File folder = new File(Properties.mibFilesPath);

        List<File> folderFiles = new ArrayList<>(Arrays.asList(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".mib") && pathname.isFile() && !persisted.contains(name);
            }
        })));

        Set<MibFile> mibFileSet = initializeMibsIndexes(root, folderFiles, Properties.mibFilesPath);

        mibFileSet = mibFileSet.stream()
                .filter(var -> !persisted.contains(var.getFileName()))
                .collect(Collectors.toSet());

        mibFileService.getMibFileDAO().saveAll(mibFileSet);
    }

    // CARREGA OS MODULOS DOS MIBFILE NA ARVORE QUE É PASSADA
    public TreeNode<Integer, String, MibFileOid> populateIanaTree(Map<String, Pair<String, Integer>> modules, MibFile mibFile,
                                                                  TreeNode<Integer, String, MibFileOid> root) {
        Map<String, Pair<String, Integer>> modulesAux = new HashMap<>();

        for (Map.Entry<String, Pair<String, Integer>> entry : modules.entrySet()) {
            TreeNode<Integer, String, MibFileOid> parentNode = root.findParentNode(entry.getValue().getKey());
            if (parentNode != null) {
                String oidPath = parentNode.getPath() + "." + entry.getValue().getValue();
                if (mibFile.getRootOID() == null || mibFile.getRootOID().length() > oidPath.length()) {
                    mibFile.setRootOID(oidPath);
                }
                parentNode.addChildren(entry.getValue().getValue(), oidPath,
                        new MibFileOid(parentNode.getPath() + "." + entry.getValue().getValue(), entry.getKey(), mibFile));
            } else {
                modulesAux.put(entry.getKey(), entry.getValue());
            }
        }
        if (modulesAux.size() > 0 && modulesAux.size() != modules.size()) {
            root = populateIanaTree(modulesAux, mibFile, root);
        }
        return root;
    }

    // INICIALIZA OS OBECT IDENTIFIERS DOS ARQUIVOS MIB QUE ESTÃO NA PASTA RAIZ MIB DA APLICAÇÃO
    // É CHAMADO APENAS NA INICIALIZAÇÃO DO APP
    public Set<MibFile> initializeMibsIndexes(TreeNode<Integer, String, MibFileOid> root, List<File> folderFiles, String filesPath) {
        Set<MibFile> newMibFiles = new HashSet<MibFile>();
        List<String> persisted = mibFileService.getMibFileDAO().getMibFileRepository().findAllNames();

        for (File file : folderFiles) {
            if (!persisted.contains(file.getName()) && !file.equals(filesPath)) {
                newMibFiles.add(new MibFile(file.getName(), filesPath + file.getName()));
            }
        }

        newMibFiles = loadMibIndexes(newMibFiles, root.getMibFileComposition(), root, filesPath);
        return newMibFiles;
    }

    // RESOLVE AS DEPENDÊNCIAS DOS ARQUIVOS E REALIZA A CHAMADA PARA ACHAR OS OBECT IDENTIFIERS
    public Set<MibFile> loadMibIndexes(Set<MibFile> toResolve, Set<MibFile> resolved, TreeNode<Integer, String, MibFileOid> root, String filesPath) {
        Set<MibFile> unresolved = new HashSet<>();
        boolean didResolvedSomething = false;
        for (MibFile var : toResolve) {
            if (!var.getFileName().equals(Properties.mibFileRoot)) {
                MibFile file = listMibFileObjectIdentifier(var, resolved, root, filesPath);
                if (file != null && file.getUnresolvedDependencies().size() > 0) {
                    unresolved.add(file);
                    Set<MibFile> dependencies = loadMibIndexes(file.getUnresolvedDependencies(), resolved, root, filesPath);
                    if (dependencies.size() > 0) {
                        resolved.addAll(dependencies);
                        didResolvedSomething = true;
                    }
                } else if (file != null) {
                    resolved.add(file);
                    didResolvedSomething = true;
                }
            }
        }
        if (didResolvedSomething && unresolved.size() > 0) {
            loadMibIndexes(unresolved, resolved, root, filesPath);
        }
        return resolved;
    }

    // DEFINE O ROOT OID DO MIBFILE, OU SEJA, O OID DE MENOR COMPRIMENTO É A RAIZ
    private MibFile calcuteIndex(Map<String, Pair<String, Integer>> modules, MibFile mibFile, TreeNode<Integer, String, MibFileOid> root) {
        Map<String, Pair<String, Integer>> modulesAux = new HashMap<>();
        for (Map.Entry<String, Pair<String, Integer>> entry : modules.entrySet()) {
            TreeNode<Integer, String, MibFileOid> parentNode = root.findParentNode(entry.getValue().getKey());
            if (parentNode != null) {
                String oidPath = parentNode.getPath() + "." + entry.getValue().getValue();
                int dotCount = StringUtils.countMatches(oidPath, ".");
                if (mibFile.getRootOID() == null || StringUtils.countMatches(mibFile.getRootOID(), ".") >= dotCount) {
                    if (mibFile.getRootOID() != null && StringUtils.countMatches(mibFile.getRootOID(), ".") == dotCount) {
                        mibFile.setRootOID(parentNode.getPath());
                    } else {
                        mibFile.setRootOID(oidPath);
                    }
                }
            } else {
                modulesAux.put(entry.getKey(), entry.getValue());
            }
        }
        if (modulesAux.size() > 0 && modulesAux.size() != modules.size()) {
            return calcuteIndex(modulesAux, mibFile, root);
        }
        return mibFile;
    }

    // FAZ A LEITURA DO ARQUIVO .mib PARA ENCONTRAR OS OBJECT IDENTIFIER, ESTRUTURA PRINCIPAL e DEPENDÊNCIAS
    private MibFile listMibFileObjectIdentifier(MibFile mibFile, Set<MibFile> resolvedMibFiles, TreeNode<Integer, String, MibFileOid> root, String filesPath) {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filesPath + mibFile.getFileName());
            br = new BufferedReader(fr);
            Map<String, Pair<String, Integer>> modules = new HashMap<>();
            Map<String, Set<Pair<String, MibParsingSyntax>>> sequences = new HashMap<>();
            Map<String, MibParsingSyntax> conventions = new HashMap<>();

            String line;
            String lastLine = new String();
            String fileIdentityIdentifier = null;
            MibParsingStep step = MibParsingStep.initializeImports;
            Pair<String, Pair<String, Integer>> result = null;
            SequenceObject sequenceObject = null;
            Pair<String, MibParsingSyntax> textualConvention = null;

            READ_MODULE:
            while ((line = br.readLine()) != null) {
                switch (step) {
                    case initializeImports:
                        if (line.trim().startsWith("IMPORTS")) {
                            step = MibParsingStep.findImports;
                        }
                        if (line.contains("OBJECT IDENTIFIER ::=")) {
                            step = MibParsingStep.findObjectIdentifier;
                            result = findObjectIdentifier(line);
                            if (result != null) {
                                modules = insertModule(modules, result.getKey(), result.getValue());
                            }
                        } else if (line.endsWith("MODULE-IDENTITY")) {
                            step = MibParsingStep.findModuleIdentity;
                            result = findModuleIdentifier(line, fileIdentityIdentifier);
                            if (result != null) {
                                modules = insertModule(modules, result.getKey(), result.getValue());
                                fileIdentityIdentifier = result.getKey();
                                step = MibParsingStep.findModuleIdentityValue;
                            }
                        } else {
                            break;
                        }
                    case findImports:
                        if (line.contains("FROM")) {
                            String[] targetArray = line.trim().split(" ");
                            String lastWord = "";
                            for (String word : targetArray) {
                                if (lastWord.equals("FROM")) {
                                    if (word.endsWith(";")) {
                                        word = word.replace(";", "");
                                        step = mibFile.getFileName().startsWith("RFC") ?
                                                MibParsingStep.findObjectIdentifier : MibParsingStep.findModuleIdentity;
                                    }
                                    MibFile dependencie = new MibFile(word + ".mib", Properties.mibFilesPath + word + ".mib");
                                    if (!resolvedMibFiles.contains(dependencie)) {
                                        mibFile.getUnresolvedDependencies().add(dependencie);
                                    } else {
                                        mibFile.getUnresolvedDependencies().remove(dependencie);
                                        mibFile.getDependencies().add(dependencie);
                                    }
                                    lastWord = "";
                                }
                                if (word.equals("FROM")) {
                                    lastWord = word;
                                }
                            }
                        }
                        break;
                    case findModuleIdentity:
                        result = findModuleIdentifier(line, fileIdentityIdentifier);
                        if (result != null) {
                            modules = insertModule(modules, result.getKey(), result.getValue());
                            fileIdentityIdentifier = result.getKey();
                            step = MibParsingStep.findModuleIdentityValue;
                        }
                        break;
                    case findModuleIdentityValue:
                        if (line.contains("::=")) {
                            String target = StringUtils.substringBetween(line, "{", "}");
                            String[] targetArray = target.trim().split(" ");
                            String parent = targetArray[0];
                            Integer value = Integer.parseInt(targetArray[1]);
                            modules = insertModule(modules, fileIdentityIdentifier, new Pair<String, Integer>(parent, value));
                            step = MibParsingStep.findObjectIdentifier;
                            fileIdentityIdentifier = null;
                        }
                        break;
                    case findObjectIdentifier:
                        result = findObjectIdentifier(line);
                        if (result == null && lastLine.trim().endsWith("OBJECT IDENTIFIER") && line.trim().startsWith("::=")) {
                            result = findObjectIdentifier(lastLine.trim() + (" ") + (line.trim()));
                        }
                        if (result != null) {
                            modules = insertModule(modules, result.getKey(), result.getValue());
                        } else {
                            if ((line.contains("::=") && line.contains("SEQUENCE") && line.contains("{"))) {
                                sequenceObject = new SequenceObject(line.trim().split(" ")[0]);
                                step = MibParsingStep.findSequenceObjects;
                            } else if (line.contains("SEQUENCE") && lastLine.contains("::=")) {
                                String[] targetArray = lastLine.trim().split(" ");
                                for (String s : targetArray) {
                                    if (!s.equals(" ") && !s.equals("")) {
                                        sequenceObject = new SequenceObject(s);
                                        step = MibParsingStep.findSequenceObjects;
                                        break;
                                    }
                                }
                            }
                            if (line.contains("::=") && line.contains("TEXTUAL-CONVENTION")) {
                                String aux = line.trim().split(" ")[0].trim();
                                textualConvention = new Pair<>(aux, null);
                                step = MibParsingStep.findTextualConvention;
                            }
                        }
                        break;
                    case findSequenceObjects:
                        if (!lastLine.trim().startsWith("SEQUENCE") && !lastLine.endsWith(",")
                                && !line.trim().equals("}") && !lastLine.contains(sequenceObject.header)
                                && !line.trim().equals("") && !lastLine.trim().equals("")) {
                            MibParsingSyntax syntax = null;
                            try {
                                String aux = line.trim();
                                if (aux.contains(" ")) {
                                    aux = aux.split(" ")[0];
                                }
                                for (MibParsingSyntax var : MibParsingSyntax.values()) {
                                    aux = aux.replaceAll(",", "");
                                    if (var.name().equals(aux)) {
                                        syntax = var;
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                            }
                            Pair<String, MibParsingSyntax> object = new Pair<>(lastLine.trim(), syntax);
                            sequenceObject.objects.add(object);
                        } else if (line.trim().equals("}")) {
                            sequences.put(sequenceObject.header, sequenceObject.objects);
                            step = MibParsingStep.findObjectIdentifier;
                        } else if (line.endsWith(",") && line.trim().split(" ").length == 2
                                && !line.trim().equals("") && !lastLine.trim().equals("")) {
                            String[] array = line.trim().split(" ");
                            MibParsingSyntax syntax = null;
                            try {
                                String aux = array[1].replaceAll(",", "").trim();
                                for (MibParsingSyntax var : MibParsingSyntax.values()) {
                                    if (var.name().equals(aux)) {
                                        syntax = var;
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                            }
                            Pair<String, MibParsingSyntax> object = new Pair<>(array[0].trim(), syntax);
                            sequenceObject.objects.add(object);
                        }
                        break;
                    case findTextualConvention:
                        if (line.trim().startsWith("SYNTAX")) {
                            MibParsingSyntax syntax = null;
                            for (String s : line.trim().split(" ")) {
                                if (!s.equals(" ") || !s.equals("SYNTAX") || s.equals("")) {
                                    try {
                                        for (MibParsingSyntax var : MibParsingSyntax.values()) {
                                            if (var.name().equals(s)) {
                                                syntax = var;
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                if (syntax != null) {
                                    break;
                                }
                            }
                            conventions.put(textualConvention.getKey(), syntax);
                            step = MibParsingStep.findObjectIdentifier;
                        }
                        break;
                }
                lastLine = line;
            }
            mibFile = calcuteIndex(modules, mibFile, root);
            mibFile.setModules(modules);
            mibFile.setSequences(sequences);
            mibFile.setConventions(conventions);
            return mibFile;
        } catch (Exception e) {
            return null;
        }
    }

    private Pair<String, Pair<String, Integer>> findObjectIdentifier(String line) {
        if (line.contains("OBJECT IDENTIFIER ::=")) {
            try {

                String target = StringUtils.substringBetween(line.trim(), "{", "}");
                String[] targetArray = target.trim().split(" ");
                String parent = targetArray[0];
                Integer value = Integer.parseInt(targetArray[1]);
                String nodeIdentifier = line.trim().split(" ")[0];
                if (!nodeIdentifier.startsWith("--")) {
                    return new Pair<String, Pair<String, Integer>>(nodeIdentifier, new Pair<String, Integer>(parent, value));
                }
            } catch (Exception e) {
                Logging.adminLog(MibManager.class, e.getMessage());
            }
        }
        return null;
    }

    private Pair<String, Pair<String, Integer>> findModuleIdentifier(String line, String fileIdentityIdentifier) {
        if (line.endsWith("MODULE-IDENTITY")) {
            fileIdentityIdentifier = line.substring(0, line.indexOf("MODULE-IDENTITY")).trim();
            return new Pair<String, Pair<String, Integer>>(fileIdentityIdentifier, null);
        } else {
            return null;
        }
    }

    private class SequenceObject {
        private String header;
        private Set<Pair<String, MibParsingSyntax>> objects = new LinkedHashSet<>();

        public SequenceObject(String header) {
            this.header = header;
        }
    }

    private Map<String, Pair<String, Integer>> insertModule(Map<String, Pair<String, Integer>> modules, String key, Pair<String, Integer> object) {
        if (object != null && object.getValue() != 0) {
            modules.put(key, object);
        }
        return modules;
    }
}
