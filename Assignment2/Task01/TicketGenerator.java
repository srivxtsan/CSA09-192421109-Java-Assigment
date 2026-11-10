// Task 1: String vs StringBuilder vs StringBuffer
public class TicketGenerator {
    public static void main(String[] args) {
        int fields = 10000;
        
        // String test
        long start1 = System.nanoTime();
        String s = "";
        for(int i=0; i<fields; i++) s += "F" + i;
        long end1 = System.nanoTime();
        
        // StringBuilder test
        long start2 = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<fields; i++) sb.append("F").append(i);
        long end2 = System.nanoTime();
        
        System.out.println("String time (ms): " + (end1-start1)/1e6);
        System.out.println("StringBuilder time (ms): " + (end2-start2)/1e6);
    }
}
