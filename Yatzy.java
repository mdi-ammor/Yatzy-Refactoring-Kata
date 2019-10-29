
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yatzy {
    public static int chance(int d1, int d2, int d3, int d4, int d5) {
    	int numbers[] = { d1, d2, d3, d4, d5}; 
    	
    	return IntStream.of(numbers).sum();
    }

    public static int yatzy(int... dice) {
    	
	final int[] counts = new int[6];
		
    	IntStream.of(dice).forEach(e -> counts[e-1]++);
    	
    	return IntStream.of(counts).filter(x -> x==5).findAny().isPresent() ? 50 : 0;
	}

    public static int ones(int d1, int d2, int d3, int d4, int d5) {
    	
    	
    	int numbers[] = { d1, d2, d3, d4, d5};
    	
    	Long res = IntStream.of(numbers).filter(n -> n==1).count();
    	
    	return res.intValue();
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
    	int numbers[] = { d1, d2, d3, d4, d5};

    	Long res = IntStream.of(numbers).filter(n -> n==2).count();
    	
    	return res.intValue() * 2;
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
    	int numbers[] = { d1, d2, d3, d4, d5};

    	Long res = IntStream.of(numbers).filter(n -> n==3).count();
    	
    	return res.intValue() * 3;
    }

    protected int[] dice;
    public Yatzy(int d1, int d2, int d3, int d4, int _5) 
    {
        dice = new int[5];
        dice[0] = d1;
        dice[1] = d2;
        dice[2] = d3;
        dice[3] = d4;
        dice[4] = _5;
    }

    public int fours() {
    	
    	Long res = IntStream.of(dice).filter(n -> n==4).count();
    	return res.intValue() * 4;
    }

    public int fives() {
    	Long res = IntStream.of(dice).filter(n -> n==5).count();
    	return res.intValue() * 5;
    }

    public int sixes() {
    	Long res = IntStream.of(dice).filter(n -> n==6).count();
    	return res.intValue() * 6;
    }

    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
    	
		int numbers[] = { d1, d2, d3, d4, d5 };

		int[] counts = new int[6];
		IntStream.of(numbers).forEach(e -> counts[e - 1]++);
		
        int at;
        for (at = 0; at != 6; at++) {
            if (counts[6-at-1] >= 2) {
                return (6-at)*2;
            }
        }
		
		AtomicInteger i = new AtomicInteger();
		int index = IntStream.of(counts).peek(v -> i.incrementAndGet())
										.anyMatch(n -> n >= 2) ? i.get() : -1;

		return (6 - index) * 2;
    }

    public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
        List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
        
        AtomicInteger n = new AtomicInteger(0);
        AtomicInteger score = new AtomicInteger(0);
        IntStream.range(0, 6)
        	.filter(i -> valuesList.get(6-i-1) >= 2)
        	.forEach(item -> {
        		n.incrementAndGet();
        		score.addAndGet(item*2);
        	});
        
        if (n.get() == 2)
            return score.get() * 2;
        else
            return 0;
        
    }

    public static int four_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
    	List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
    	AtomicInteger res = new AtomicInteger(0);
    	IntStream.range(0, 6)
    		.filter(i -> valuesList.get(i) >= 4)
    		.forEach(item -> {
    			res.addAndGet((item*4)+4);
    		});

    	if(res.get()!=0) {
    		return res.get();
    	}
    	return 0;
    }

    public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
    	List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
        for (int i = 0; i < 6; i++)
            if (valuesList.get(i) >= 3)
                return (i+1) * 3;
        return 0;
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
    	List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
    	if (IntStream.range(0, valuesList.size()).filter(i -> valuesList.get(i) == i).findFirst().isPresent()) {
    		return 15;
		}
        return 0;
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5)  {
    	List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
    	if (IntStream.range(0, valuesList.size()).filter(i -> valuesList.get(i) == i).findFirst().isPresent()) {
    		return 20;
		}
        return 0;
    }

    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
    	AtomicBoolean value2 = new AtomicBoolean(false);
    	AtomicInteger at2 = new AtomicInteger(0);
    	AtomicBoolean value3 = new AtomicBoolean(false);
    	AtomicInteger at3 = new AtomicInteger(0);

        List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);

        IntStream.range(0, 6)
        	.filter(i -> valuesList.get(i) == 2)
        	.forEach(item -> {
        		value2.set(true);
        		at2.addAndGet(item+1);
        	});
        IntStream.range(0, 6)
        	.filter(i -> valuesList.get(i) == 3)
        	.forEach(item -> {
        		value3.set(true);
        		at3.addAndGet(item+1);
        	});
        
        if (value2.get() && value3.get())
            return at2.get() * 2 + at3.get() * 3;
        else
            return 0;
    }
   
    private static List<Integer> buildValuesList(int d1, int d2, int d3, int d4, int d5) {
		int[] list = new int[6];
         List<Integer> parametersList = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5));
         List<Integer> valuesList = Arrays.stream(list).boxed().collect(Collectors.toList());
         IntStream.range(0, valuesList.size()-1)
         	.forEach(i -> valuesList.set(parametersList.get(i) - 1, valuesList.get(parametersList.get(i) - 1) + 1)
         	);
		return valuesList;
	}
}

