package br.edu.router.util;

import br.edu.router.benchmark.BenchmarkResult;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {
    public static void exportToCSV (List<BenchmarkResult> results, String filename) {
        String path = "results/" + filename;
        try (FileWriter fw = new FileWriter(path);
            PrintWriter pw = new PrintWriter(fw)) {
                
            pw.println("Arvore,Volume,Tempo_Insercao_ns,Tempo_Busca_ns,Tempo_Delecao_ns,Rotacoes");

            for(BenchmarkResult res : results) {
                pw.printf("%s,%d,%d,%d,%d,%d\n",
                    res.getTreeName(),
                    res.getVolume(),
                    res.getInsertionTime(),
                    res.getSearchTime(),
                    res.getDeletionTime(),
                    res.getRotations()
                );
            }
            System.out.println("Dados exportados com sucesso para: " + path);
            } catch (IOException e) {
                System.err.println("Erro ao exportar CSV: " + e.getMessage());
        }
    }      
}

