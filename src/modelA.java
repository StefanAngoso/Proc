import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author he
 */
public class modelA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//      model1and2(50,0,0.7,0.3,10000);
    	/*model3(total_pc, proba_cure, proba_infect, step)*/
    	//model3(50,1,1,10000);
    	/*
    	 * p1 = network_size
    	 * p2 = proba_cure
    	 * p3 = proba_infect
    	 * p4 = step
    	 */
    	model4(10, 0.3, 0.8, 100000);
    }
    
    /**
     * 
     * @param total
     * @param nb_infect
     * @param cure = p <1
     * @param infect = q <1
     * @param step 
     */
    public static void model1and2(int total, int nb_infect, double cure, double infect, int step){
        if(total < nb_infect){
            
        }else{
            int s=step;
            int sum_infected_each_step=0;
            double rand_state;
            double rand_choice;
            while(s>0){
                rand_state = (double)randomWithRange(0,100)/100;
                rand_choice = (double)randomWithRange(0,100)/100;
                
                if(nb_infect==0){
                    if(infect>=rand_state){
                        nb_infect++;
                    }
                }else if(nb_infect==total){
                    if(cure>=rand_state){
                        nb_infect--;
                    }
                }else{
                    System.out.println("rand_choice = "+rand_choice);
                    if(((double)(total-nb_infect)/total)>=rand_choice){
                        if(infect>=rand_state){
                            nb_infect++;
                        }
                    } else {
                        if(cure>=rand_state){
                            nb_infect--;
                        }
                    }
                }
                s--;
                System.out.println("rand_state = "+rand_state);
                System.out.println("nb_infect = "+nb_infect);
                sum_infected_each_step+=nb_infect;
            }
//            System.out.println("nb_infect = "+nb_infect+", with step = "+step);
            System.out.println("Modèle 2 :");
            System.out.println("Probabilité d'infecter = "+infect);
            System.out.println("Probabilité de guérir = "+cure);
            System.out.println("Total des ordis sur le réseau = "+total);
            System.out.println("Somme des infectés sur chaque répétition= " + (double)(sum_infected_each_step));
            System.out.println("Répétition = " + (double)(step)+" fois");
            System.out.println("Moyenne d'ordis infectés = ("+sum_infected_each_step+"/"+step+") = " + (double)((double)sum_infected_each_step/(double)step)+"/"+total+" infectés");
            
        }
    }
    
    public static int randomWithRange(int min, int max)
    {
       int range = (max - min) + 1;     
       double test = (Math.random() * range) + min;
       return (int)(Math.random() * range) + min;
    }
    
    /**
     * 
     * @param total
     * @param cure	
     * @param infect
     * @param step
     *	tout pc = saint
     */
    public static void model3(int total, double cure, double infect, int step){
    	
    	int[] pc_array = new int[total];
    	/*position du virus*/
    	int index_infect = 0;
    	/*position du anti-virus*/
    	int index_cure = total-1;
    	int sum_total_step=0;
    	int s=step;
    	
    	int a = index_cure;
    	int v = index_infect;
    	
    	/*random proba*/
    	double proba_infect, proba_cure;
    	do{
    		proba_infect=(double)randomWithRange(0, 100)/100;
    		/*infection possible*/
    		if(infect>=proba_infect){
    			pc_array[index_infect]=1;
    		}
    		
    		/*guérison possible */
    		proba_cure=(double)randomWithRange(0, 100)/100;
    		if(cure>=proba_cure){
    			pc_array[index_cure]=0;
    		}
    		
    		/*déplacement next voisin (proba=1/2), pour le virus et l'antivirus*/
    		index_infect = next_index(index_infect, total);
    		index_cure = next_index(index_cure, total);
    		s--;
    		/*somme le nombre d'infecté pour chaque état*/
    		sum_total_step+=count_infected(pc_array);
    	}while(s>0);
    	System.out.println("Modèle 3 : ");
    	System.out.println("Position Virus = "+v);
    	System.out.println("Position Anti-virus = "+a);
    	System.out.println("Probabilité d'infecter = "+infect);
    	System.out.println("Probabilité de guérir = "+cure);
    	System.out.println("Total des ordis sur le réseau = "+total);
        System.out.println("Somme des infectés sur chaque répétition= " + (double)(sum_total_step));
        System.out.println("Répétition = " + (double)(step)+" fois");
    	System.out.println("Moyenne d'ordis infectés = "+(double)sum_total_step/(double)step+ "/"+ total+" pc");
    }
    
    
    /**
     * genere la position suivante du virus/antivirus de façon aleatoire
     * @param a
     * @param total
     * @return
     */
    public static int next_index(int a, int total){
    	double proba = (double)randomWithRange(0, 100)/100;
    	if(0.5>=proba){
    		if(a==0){
    			a=total-1;
    			return a;
    		}
    		a--;
    		return a;
    	}else{
    		if(a==total-1){
    			return 0;
    		}
    		a++;
    		return a;
    	}
    }
    
    /**
     * Tous les ordis sont connectés entre eux
     * Chaque ordi infecté peut ou pas infecter tous les autres ordis
     * Chaque ordi a un antivirus, dont la performance dépend du nombre de fois qu'il est infecté
     * @param network_size
     * @param curable : probabilité de se désinfecter, qui peut diminuer au cours du temps
     * @param infectable
     * @param step
     */
    public static void model4(int network_size, double curable, double infectable, int step){
    	ArrayList<Computer> network = create_network(network_size);
    	int s = step-1;
    	int computer_aimed;
    	double proba_cure;
    	int actions_within_step;
    	int total = 0;
    	
    	ArrayList<Integer> infected_ids = new ArrayList<Integer>();
    	
    	//choix aleatoire d'un ordi C au sein du reseau
		computer_aimed = randomWithRange(0, network_size-1);
		//probabilité d'infecter C
		if(1>(double)randomWithRange(0, 100)/100){
			network.get(computer_aimed).setInfected(true);
			network.get(computer_aimed).addInfectionTimes();
		}
    	
		total+=count_infected(network);
    	
    	while(s>0){
    		//Pour une étape, chaque ordi infecté, va essayer d'infecter tous les autres
    		//Ex:à une étape donnée, il y a 2 infecté sur 100, donc chaque infecté va essayer d'infecter les 98 autres [boucle 2 fois]
    		infected_ids = getAllInfectedId(network);
    		for(Integer infected_id:infected_ids){
				computer_aimed = randomWithRange(0, network_size-1);
    			if(infected_id!=computer_aimed){
    				if(infectable>(double)randomWithRange(0, 100)/100){
    					network.get(computer_aimed).setInfected(true);
            			network.get(computer_aimed).addInfectionTimes();
    				}
    				
    			}
    		}
    		
//    		actions_within_step = count_infected(network);
//    		while(actions_within_step>0){
//    			for(Computer c : network){
//    				if(infectable>(double)randomWithRange(0, 100)/100){
//    					c.setInfected(true);
//            			c.addInfectionTimes();
//    				}
//    			}
//    			
//    			actions_within_step--;
//    		}
    		
    		
    		
    		s--;
    		
    		//Pour une étape, chaque ordi infecté, va essayer de se désinfecter
    		for(Computer c : network){
    			if(c.isInfected()){
    				//tout dépend de son nombre d'infection
        			//ex:curable = 50%, l'ordi est infecté 2 fois, donc il peut se désinfecter avec 25%
    				proba_cure = curable/(c.getInfection_times());
    				if(proba_cure>(double)randomWithRange(0, 100)/100){
    					c.setInfected(false);
    					//c.setInfection_times(0);
    					c.subInfectionTimes();
    				}
    			}
    		}
    		
    		total+=count_infected(network);
    		
    	}
    	
    	System.out.println("Moyenne d'ordi infecté sur "+step+"step = "+(double)total/step);
    	System.out.println(network.toString());
    }
    
    public static ArrayList<Integer> getAllInfectedId(ArrayList<Computer> network){
    	ArrayList<Integer> result = new ArrayList<Integer>();
    	for(int i=0; i<network.size();i++){
    		if(network.get(i).isInfected()){
    			result.add(i);
    		}
    	}
    	return result;
    }
    
    /**
     * Compte le nombre d'infecté sur le réseau
     * @param tab
     * @return
     */
    public static int count_infected(int[] tab){
    	int count=0;
    	for(int a:tab){
    		if(a==1){
    			count++;
    		}
    	}
    	return count;
    }
    
    public static int count_infected(ArrayList<Computer> network){
    	int result = 0;
    	
    	for(Computer c : network){
    		if(c.isInfected()){
    			result++;
    		}
    	}
    	
    	return result;
    }
    
    public static int count_infected_times(ArrayList<Computer> network){
    	int result = 0;
    	for(Computer c : network){
    		result+=c.getInfection_times();
    	}
    	
    	return result;
    }
    
    /**
     * Créer une liste d'ordi
     * @param network_size
     * @return
     */
    public static ArrayList<Computer> create_network(int network_size){
    	ArrayList<Computer> network = new ArrayList<Computer>();
    	for(int i=0; i<network_size;i++){
    		network.add(new Computer(false,0));
    	}
    	return network;
    }
    
    
    
    
}

/**
 * Chaque ordi a un statut, et son nombre de fois infecté
 * @author Administrateur
 *
 */
class Computer {
	private boolean infected;
	
	private int infection_times;
	
	public Computer(boolean infected, int infection_times) {
		this.infected = infected;
		this.infection_times = infection_times;
	}
	
	public int getInfection_times() {
		return infection_times;
	}
	public void setInfection_times(int infection_times) {
		this.infection_times = infection_times;
	}
	
	public void addInfectionTimes(){
		this.infection_times = this.infection_times+1;
	}
	
	public void subInfectionTimes(){
		this.infection_times = this.infection_times-1;
	}

	public boolean isInfected() {
		return infected;
	}

	public void setInfected(boolean infected) {
		this.infected = infected;
	}

	@Override
	public String toString() {
		return "Computer [infected=" + infected + ", infection_times="
				+ infection_times + "]\n";
	}
	
	
}


