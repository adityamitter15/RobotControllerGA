import java.util.Random;

public class Individual {
    private int[] chromosome;
    private double fitness = -1;  //initialise to super bad value!

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
        // initialise with random genes
        Random r = new Random();
        for (int gene = 0; gene < chromosomeLength; gene++) {
            if(r.nextBoolean()) {
                setGene(gene, 1);
            } else {
                setGene(gene, 0);
            }
        }
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public int getChromosomeLength() {
        return chromosome.length;
    }

    public void setGene(int offset, int value) {
        chromosome[offset] = value;
    }

    public int getGene(int offset) {
        return chromosome[offset];
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double newValue) {
        fitness = newValue;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int gene: chromosome) {
            output.append(gene);
        }
        return output.toString();
    }
}

