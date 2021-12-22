package game;

//Class fournie : Chronometre 
 
public class Chronometre {
    
    
    private long begin;
    private long end;
    private long current;
    private int limite;
    
    Chronometre(int limite){
        this.end=0;
        this.begin=0;
        this.limite=limite;
        this.current = 0;
    }

 
    public void start(){
        begin = System.currentTimeMillis();
    }
  
    public void stop(){
        end = System.currentTimeMillis();
    }
    
    public long getTime() {
        return end-begin;
    }
 
    public long getMilliseconds() {
        return end-begin;
    }
 
    public int getSeconds() {
        return (int) ((end - begin) / 1000.0);
    }
 
    public double getMinutes() {
        return (end - begin) / 60000.0;
    }
 
    public double getHours() {
        return (end - begin) / 3600000.0;
    }
    
    public int getLimite() {
        return this.limite;
    }
    /**
    * Method to know if it remains time.
    */
    public boolean remainsTime() {
        current = System.currentTimeMillis();
        int timeSpent;
        timeSpent = (int) ((current - this.begin)/1000.0);
        return (!(timeSpent == limite));
    }
    
}