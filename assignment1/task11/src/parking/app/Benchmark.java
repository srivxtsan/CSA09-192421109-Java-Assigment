package parking.app;

import java.util.*;

/** Task 11 alternative: find an active ticket on exit - array vs ArrayList vs HashMap. */
public class Benchmark {
    record T(String reg, int no) {}
    public static void main(String[] args) {
        int[] sizes = {1_000, 10_000, 100_000};
        System.out.printf("%-9s %-18s %-18s %-18s%n", "n", "array O(n) ns/op", "ArrayList O(n)", "HashMap O(1)");
        for (int n : sizes) {
            T[] arr = new T[n]; List<T> list = new ArrayList<>(); Map<String, T> map = new HashMap<>();
            for (int i = 0; i < n; i++) { T t = new T("TN" + i, i); arr[i] = t; list.add(t); map.put(t.reg(), t); }
            int q = 2000; String[] keys = new String[q]; Random r = new Random(1);
            for (int i = 0; i < q; i++) keys[i] = "TN" + r.nextInt(n);
            for (int warm = 0; warm < 2; warm++) { find(arr, keys); find(list, keys); find(map, keys); }
            long t0 = System.nanoTime(); int c1 = find(arr, keys);
            long t1 = System.nanoTime(); int c2 = find(list, keys);
            long t2 = System.nanoTime(); int c3 = find(map, keys);
            long t3 = System.nanoTime();
            System.out.printf("%-9d %-18d %-18d %-18d  (checks %d/%d/%d)%n", n, (t1 - t0) / q, (t2 - t1) / q, (t3 - t2) / q, c1, c2, c3);
        }
        Runtime rt = Runtime.getRuntime();
        System.out.println("Space: array n refs (~4-8 B each), ArrayList n refs + spare capacity (<=1.5x), HashMap n entries (~32+ B each) + table");
    }
    static int find(T[] a, String[] ks) { int c = 0; for (String k : ks) for (T t : a) if (t.reg().equals(k)) { c++; break; } return c; }
    static int find(List<T> a, String[] ks) { int c = 0; for (String k : ks) for (T t : a) if (t.reg().equals(k)) { c++; break; } return c; }
    static int find(Map<String, T> m, String[] ks) { int c = 0; for (String k : ks) if (m.get(k) != null) c++; return c; }
}
