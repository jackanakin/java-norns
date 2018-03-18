package com.ark.norns.dataStructure;

public enum MibParsingStep {
    // find OBJECT IDENTIFIER, MODULE IDENTITY, IMPORT DEPENDENCIES
    initializeImports, findImports, findModuleIdentity, findModuleIdentityValue, findObjectIdentifier, findSequenceObjects, findTextualConvention,
    // find OBJECT-TYPE
    findObjectType, findObjectTypeSyntax, findObjectTypeAccess, findObjectTypeStatus, findObjectTypeDescription, findingObjectTypeDescription, findObjectTypeIdentity;
    ;
}