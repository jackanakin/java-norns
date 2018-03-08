package com.ark.norns.application;

import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFileOid;
import com.ark.norns.dataStructure.MibParsingStep;
import com.ark.norns.dataStructure.TreeNode;
import com.ark.norns.service.MibFileService;
import com.ark.norns.util.OidUtil;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class MibManager {
    private static MibFileService mibFileService;

    private static String ianaTreeFileName = "yggdrasil.mib";
    public static MibFile yggdrasil = new MibFile(ianaTreeFileName);
    public static Set<MibFile> mibFiles = new HashSet<>();
    public static TreeNode<Integer, String, MibFileOid> root = new TreeNode(1, "1", new MibFileOid("1", "iso", yggdrasil), 0);

    @Autowired
    public MibManager(MibFileService mibFileService) {
        this.mibFileService = mibFileService;
    }

    public static TreeNode<Integer, String, MibFileOid> initializeYggdrasil(TreeNode<Integer, String, MibFileOid> root) {
        String filePath = Properties.mibFilesPath + ianaTreeFileName;
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

    public static void initializeMibsIndexes() {
        File folder = new File(Properties.mibFilesPath);
        List<String> folderFiles = new ArrayList<>(Arrays.asList(folder.list()));
        Set<MibFile> newMibFiles = new HashSet<MibFile>();
        mibFiles = new HashSet<MibFile>(mibFileService.getMibFileDAO().findAll());

        MibFile auxMibFile = new MibFile();
        for (String file : folderFiles) {
            auxMibFile.setFileName(file);
            if (!mibFiles.contains(auxMibFile) && !file.equals(ianaTreeFileName)) {
                newMibFiles.add(new MibFile(file));
            }
        }
        loadMibIndexes(newMibFiles);
        mibFileService.getMibFileDAO().saveAll(mibFiles);
        mibFiles = null;
    }

    private static void loadMibIndexes(Set<MibFile> filesDir) {
        Set<MibFile> filesAux = new HashSet<>();
        for (MibFile file : filesDir) {
            if (!file.getFileName().equals("yggdrasil.mib")) {
                List<MibFile> unresolved = loadMibFileIndex(file.getFileName(), Properties.mibFilesPath);
                if (unresolved != null && unresolved.size() > 0) {
                    filesAux.add(file);
                }
            }
        }
        if (filesAux.size() > 0 && filesAux.size() != filesDir.size()) {
            loadMibIndexes(filesAux);
        }
    }

    public ObjectIdentifierProcess listMibFileObjectIdentifier(BufferedReader br, String fileName) throws Exception{
        Map<String, Pair<String, Integer>> modules = new HashMap<>();
        String line;
        String lastLine = new String();
        String fileIdentityIdentifier = null;
        MibParsingStep step = MibParsingStep.initializeImports;
        List<MibFile> importDependencies = new ArrayList<>();
        Pair<String, Pair<String, Integer>> result = null;
        MibFile mibFile = new MibFile(fileName);

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
                            modules.put(result.getKey(), result.getValue());
                        }
                    } else if (line.endsWith("MODULE-IDENTITY")) {
                        step = MibParsingStep.findModuleIdentity;
                        result = findModuleIdentifier(line, fileIdentityIdentifier);
                        if (result != null) {
                            modules.put(result.getKey(), result.getValue());
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
                                    step = fileName.startsWith("RFC") ?
                                            MibParsingStep.findObjectIdentifier : MibParsingStep.findModuleIdentity;
                                }
                                MibFile dependencie = new MibFile(word + ".mib");
                                if (!mibFiles.contains(dependencie)) {
                                    importDependencies.add(dependencie);
                                } else {
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
                        modules.put(result.getKey(), result.getValue());
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
                        modules.put(fileIdentityIdentifier, new Pair<String, Integer>(parent, value));
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
                        modules.put(result.getKey(), result.getValue());
                    }
                    break;
            }
            lastLine = line;
        }
        return new ObjectIdentifierProcess(importDependencies, modules);
    }

    public static List<MibFile> loadMibFileIndex(String fileName, String filePath) {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filePath + fileName);
            br = new BufferedReader(fr);

            Map<String, Pair<String, Integer>> modules = new HashMap<>();
            String line;
            String lastLine = new String();
            MibFile mibFile = new MibFile(fileName);
            mibFile.setRootOID(null);
            String fileIdentityIdentifier = null;
            MibParsingStep step = MibParsingStep.initializeImports;
            List<MibFile> importDependencies = new ArrayList<>();
            Pair<String, Pair<String, Integer>> result = null;
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
                                modules.put(result.getKey(), result.getValue());
                            }
                        } else if (line.endsWith("MODULE-IDENTITY")) {
                            step = MibParsingStep.findModuleIdentity;
                            result = findModuleIdentifier(line, fileIdentityIdentifier);
                            if (result != null) {
                                modules.put(result.getKey(), result.getValue());
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
                                        step = fileName.startsWith("RFC") ?
                                                MibParsingStep.findObjectIdentifier : MibParsingStep.findModuleIdentity;
                                    }
                                    MibFile dependencie = new MibFile(word + ".mib");
                                    if (!mibFiles.contains(dependencie)) {
                                        importDependencies.add(dependencie);
                                    } else {
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
                            modules.put(result.getKey(), result.getValue());
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
                            modules.put(fileIdentityIdentifier, new Pair<String, Integer>(parent, value));
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
                            modules.put(result.getKey(), result.getValue());
                        }
                        break;
                }
                lastLine = line;
            }
            mibFile = calcuteIndex(modules, mibFile);
            if (mibFile.getRootOID() != null || mibFile.getFileName().startsWith("RFC") || mibFile.getFileName().startsWith("SNMP")) {
                mibFiles.add(mibFile);
            }
            if (importDependencies.size() > 0) {
                return importDependencies;
            } else {
                return null;
            }
        } catch (IOException e) {
            Logging.adminLog(MibManager.class, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                Logging.adminLog(MibManager.class, ex.getMessage());
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static MibFile populateIanaTree(Map<String, Pair<String, Integer>> modules, MibFile mibFile,
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
            return populateIanaTree(modulesAux, mibFile, root);
        }
        return mibFile;
    }

    private static MibFile calcuteIndex(Map<String, Pair<String, Integer>> modules, MibFile mibFile) {
        Map<String, Pair<String, Integer>> modulesAux = new HashMap<>();
        for (Map.Entry<String, Pair<String, Integer>> entry : modules.entrySet()) {
            TreeNode<Integer, String, MibFileOid> parentNode = root.findParentNode(entry.getValue().getKey());
            if (parentNode != null) {
                String oidPath = parentNode.getPath() + "." + entry.getValue().getValue();
                if (mibFile.getRootOID() == null || mibFile.getRootOID().length() > oidPath.length()) {
                    mibFile.setRootOID(oidPath);
                }
            } else {
                modulesAux.put(entry.getKey(), entry.getValue());
            }
        }
        if (modulesAux.size() > 0 && modulesAux.size() != modules.size()) {
            return calcuteIndex(modulesAux, mibFile);
        }
        return mibFile;
    }

    private static Pair<String, Pair<String, Integer>> findObjectIdentifier(String line) {
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

    private static Pair<String, Pair<String, Integer>> findModuleIdentifier(String line, String fileIdentityIdentifier) {
        if (line.endsWith("MODULE-IDENTITY")) {
            fileIdentityIdentifier = line.substring(0, line.indexOf("MODULE-IDENTITY")).trim();
            return new Pair<String, Pair<String, Integer>>(fileIdentityIdentifier, null);
        } else {
            return null;
        }
    }

    private class ObjectIdentifierProcess{
        public List<MibFile> importDependencies = new ArrayList<>();
        public Map<String, Pair<String, Integer>> modules = new HashMap<>();
        public ObjectIdentifierProcess(List<MibFile> importDependencies, Map<String, Pair<String, Integer>> modules) {
            this.importDependencies = importDependencies;
            this.modules = modules;
        }
    }
}
