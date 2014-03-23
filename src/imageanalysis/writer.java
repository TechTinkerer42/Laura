package examples;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Writer {
	private String filepath;
	private String line;
	private PrintWriter writer;

	
	public Writer(String filepath) throws FileNotFoundException, UnsupportedEncodingException{
		this.filepath = filepath;
		writer = new PrintWriter(filepath, "UTF-8");

	}
	
	public void setFilePath(String path){
		this.filepath = path;
	}

	
	public void writeln(String line){
		writer.println(line);
	}
	
	public void writeln(String line, String abspath) throws FileNotFoundException, UnsupportedEncodingException{
		writer = new PrintWriter(abspath, "UTF-8");
		System.out.println("new abspath, " + abspath + " added as the current path to write");
		writer.println(line);
	}
	
	public void closefile(){
		writer.checkError();
		writer.close();
	}

}
