package br.edu.router.benchmark;

import br.edu.router.model.PacketRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public static List<PacketRule> generateOrderedRules(int quantity, long seed) {
        List<PacketRule> rules = new ArrayList<>();
        Random random = new Random(seed);

        for( int i = 1; i <= quantity; i++) {
            String ipOrigem = "192.168.1." + random.nextInt(256);
            String ipDestino = "10.0.0." + random.nextInt(256);
            int prioridade = random.nextInt(10) + 1;

            rules.add(new PacketRule(i,ipOrigem, ipDestino, prioridade));
        }
        rules.sort(null);
        return rules;
    }
}
