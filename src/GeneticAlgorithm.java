public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;

    public GeneticAlgorithm(int popSize, double mutationRate, double crossoverRate, int elitismCount) {
        populationSize = popSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }

    // initialise population
    public Population initPopulation(int chromosomeLength) {
        return new Population(populationSize, chromosomeLength);
    }

    // calculate the fitness of an individual
    public double calculateFitness(Individual member) {
        // keep track of the number of 1s in the chromosome
        int correct = 0;
        for(int i=0; i<member.getChromosomeLength(); i++) {
            // score 1 for each 1!
            if (member.getGene(i) == 1) {
                correct++;
            }
        }
        // normalise for chromosome length
        double fitness = (double) correct / member.getChromosomeLength();
        return fitness;
    }

    // calculate the fitness of the whole population
    public void evaluatePopulation(Population p) {
        double totalFitness = 0.0;
        for (Individual member: p.getEveryone()) {
            double fitness = calculateFitness(member);
            member.setFitness(fitness);
            totalFitness += fitness;
        }
        // normalise for pop size
        //double popFitness = (double) totalFitness / p.size();
        p.setTotalFitness(totalFitness);
    }

    public void displayPopulation(Population p) {
        for (Individual member: p.getEveryone()) {
            System.out.print(member.toString());
            System.out.println(" f=" + calculateFitness(member));
        }
        evaluatePopulation(p);
        System.out.println("Population fitness=" + p.getPopulationFitness());
    }

    public boolean isTerminationConditionMet(Population p) {
        // return true if any single solution has perfect fitness
        for (Individual member: p.getEveryone()) {
            if (member.getFitness() == 1.0) {
                return true;
            }
        }
        return false;
    }

    public Individual selectParent(Population p) {
        // spin the roulette wheel to give a value for 0 to total fitness
        double rouletteWheelPosition = Math.random() * p.getTotalFitness();
        // choose a parent with a chance proportional to the fitness
        double spin = 0.0;
        for (Individual member: p.getEveryone()) {
            spin = spin + member.getFitness();
            if (spin >= rouletteWheelPosition) {
                return member;
            }
        }
        // default is to return the last in the pop
        return p.getIndividual(p.size() - 1);
    }

    public Population crossover(Population p) {
        // return a new generation with crossed over genes
        Population nextGeneration = new Population(p.size()); // empty pop
        p.sortByFitness();
        // loop over the existing pop
        for (int popIndex=0; popIndex<p.size(); popIndex++) {
            Individual parent1 = p.getIndividual(popIndex);
            // will we crossover?
            if (Math.random() < crossoverRate && popIndex >= elitismCount) {
                // find second parent
                Individual parent2 = selectParent(p);
                // create a 'blank' child
                Individual offspring = new Individual(parent1.getChromosomeLength());
                // uniform crossover over the whole genome
                for (int i=0; i<parent1.getChromosomeLength(); i++) {
                    if (Math.random() < 0.5) {
                        offspring.setGene(i, parent1.getGene(i));
                    } else {
                        offspring.setGene(i, parent2.getGene(i));
                    }
                }
                nextGeneration.setIndividual(popIndex, offspring);
            } else {
                // not chosen for crossover
                nextGeneration.setIndividual(popIndex, parent1);
            }
        }
        return nextGeneration;
    }

    public Population mutate(Population p) {
        // randomly mutate the genes in each individual
        Population mutatedPopulation = new Population(p.size()); // start with an empty pop
        p.sortByFitness();
        for (int popIndex=0; popIndex<p.size(); popIndex++) {
            // pluck the next member from the existing pop
            Individual member = p.getIndividual(popIndex);
            // chance to mutate for every individual (not Elites)
            for (int locus=0; locus<member.getChromosomeLength(); locus++) {
                if (Math.random() < mutationRate && popIndex > elitismCount) {
                    // flip the gene at this locus
                    int newGene = (member.getGene(locus) +1) % 2;
                    member.setGene(locus, newGene);
                }
            }
            mutatedPopulation.setIndividual(popIndex, member);
        }
        return mutatedPopulation;
    }


}

