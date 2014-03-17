package neuralnetwork;

import java.util.ArrayList;
import java.util.Iterator;


public class Neuron {
	private ArrayList<Double> InputWeights  = new ArrayList<Double>();
	//private ArrayList<Double> OutputWeights = new ArrayList<Double>();
	public ArrayList<NInput> ninputs		= new ArrayList<NInput>();
	private NeuralLayer neurallayer;
	private double delta = 0;//error term delta for each neuron. 
	private int index = 0;
	
	public Neuron(int nofInputLayers, NeuralLayer neurallayer){
		this.neurallayer = neurallayer;
		index = neurallayer.NofNeurons();
		//initialize input with default weight
		for(int i=0; i<nofInputLayers; i++){
			ninputs.add(new NInput(1.0, this));//initializing weights should be random'd and close to 0. Use an init func.
			InputWeights.add(1.0);
		}	
	}
	
	public double getdelta(){
		return delta;
	}
	
	public void setdelta(double delta){
		this.delta = delta;
	}	

	public int nofInputs(){
		return this.ninputs.size();
	}
	
	public NInput getNInput(int index){
		return ninputs.get(index);
	}
	
	public String toString(){
		return "Neuron no. " + index + "of neurallayer no. " + neurallayer.getIndex() + ", with nofinputs: " + nofInputs();
	}
	
	public ArrayList<Double> getWeights(){
		ArrayList<Double> inputweights = new ArrayList<Double>()
				;
		for(NInput n : ninputs){
			inputweights.add(n.getWeight());
		}
		return inputweights;
	}
	
	//Done:	gather all inputweights
	public double getFanIn(){
		Iterator it = getWeights().iterator();
		double fan_in = 0;
		while(it.hasNext()){
			fan_in += (double) it.next();
		}
		return fan_in;
	}

//	404: Usage not found. Deprecated until further notice.
//	//TODO: do we need output weights?
//	public double getFanOut(){
//		Iterator it = OutputWeights.iterator();
//		double fan_out = 0;
//		while( it.hasNext()){
//			fan_out += (double) it.next();
//		}
//		return fan_out;
//	}
	
	//activation function here, using sigmoid
	public double activationFunc(double netinput){
		netinput = 1/(1 + Math.exp(-netinput));	//or use tanh? Tanh: (e^x-e^-x)/(e^x + e^-x)
		return netinput;
	}
	
	public double netInputSignal(ArrayList<Double> inputs){
		//iterate through both arraylist and associate them accordingly, return the sum.
		double sum = 0;
		Iterator val_it    = inputs.iterator();
		Iterator weight_it = this.ninputs.iterator();
		if(inputs.size() != ninputs.size()){
			System.err.println("ERROR: Input amount not matched with given nof weights");
			return 0;
		}
		while(val_it.hasNext()){
			if(!weight_it.hasNext()){
				System.out.println("WARNING: non-weighted values present");
				sum += (double) val_it.next();
			}
			sum += (double) val_it.next()*(double) weight_it.next();
		}
		return sum + neurallayer.getBias();
	}
	
	public double ActivationOutput(ArrayList<Double> inputs){
		return activationFunc(netInputSignal(inputs));
	}

}//end Neuron class
