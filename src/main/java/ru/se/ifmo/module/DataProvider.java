package ru.se.ifmo.module;


public interface DataProvider {

    boolean save(CollectionService collectionService);

    boolean load(CollectionService collectionService);
}

