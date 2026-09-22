package com.learnwithdurgesh.springAILearning.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class IngestionService {
    private static final Logger logger = LoggerFactory.getLogger(IngestionService.class);
    private VectorStore vectorStore;
//    Spring ai automatically injects the vectorstore in constructor
    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    public void ingestText(String content,String documentTitle,String category){
        logger.info("Ingesting manual text for doc : ",documentTitle);
//        raw text se spring ai document object banayein + metadata add kre


        Document rawDocument = new Document(content , Map.of(
                "title",documentTitle,
                "category",category,
                "timestamp",System.currentTimeMillis()
        ));
//        2.TokenTextSplitter ----> se chunking karein
//        defaultChunkSize ---> 800 tokens
//        minChunkSizeChars=5
//        maxNumChunks=10000,
//        keepSeparator=true,


        TokenTextSplitter tokenTextSplitter = TokenTextSplitter
                .builder()
                .withChunkSize(100)
                .build();


        List<Document> chunks = tokenTextSplitter.split(rawDocument);
        logger.info("Text split into {} chunks. Saving to ChromaDB.",chunks.size());
        vectorStore.add(chunks);
        logger.info("successfully added to chromadb !");
    }
//    abhi  raw text ko document ka object banaya then usko fir ham us raw data ke chunks ko token banayenfe by tokentextsplitter
    public void ingestPDF(Resource pdfResource){
        logger.info("Start ingesting pdf",pdfResource.getFilename());
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(pdfResource);
        List<Document> rawDocuments=pagePdfDocumentReader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(200).build();

        List<Document> chunks =tokenTextSplitter.split(rawDocuments);
//        agar hamne pdf dali then hame use reader me dalna read krwalo then uske chunjks banayenge
//        okay done
        logger.info("documents splits into chunks " , chunks,rawDocuments.size());
        vectorStore.add(chunks);
        logger.info("pdf chunks are added to chromadb !");


    }


}
