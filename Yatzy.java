
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yatzy {
	protected List<Integer> dice;
	
	public Yatzy(int d1, int d2, int d3, int d4, int d5) {
		dice = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5));
	}
	
    public static int chance(int d1, int d2, int d3, int d4, int d5) {
        List<Integer> valuesList = Arrays.asList(d1, d2, d3, d4, d5);
        return valuesList.stream().mapToInt(Integer::intValue).sum();
    }

    public static int yatzy(int... dice) {
		final int[] counts = new int[6];
		List<Integer> valuesList = Arrays.stream(dice).boxed().collect(Collectors.toList());
		valuesList.forEach(item->counts[item - 1]++);
		if (IntStream.range(0, 6).filter(i -> counts[i] == 5).findFirst().isPresent()) {
			return 50;
		}
		return 0;
	}

    public static int ones(int d1, int d2, int d3, int d4, int d5) {
    	return filterAndAddValue(d1, d2, d3, d4, d5, 1);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
    	return filterAndAddValue(d1, d2, d3, d4, d5, 2);
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
    	return filterAndAddValue(d1, d2, d3, d4, d5, 3);
    }

    public int fours() {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		buildValue(atomicInteger, 4, 4);
		return atomicInteger.get();
	}
	
	public int fives() {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		buildValue(atomicInteger, 5, 5);
		return atomicInteger.get();
	}

	public int sixes() {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		buildValue(atomicInteger, 6, dice.size());
		return atomicInteger.get();
	}
	
    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
    	List<Integer> valuesList = buildValuesList(d1, d2, d3, d4, d5);
    	AtomicInteger res = new AtomicInteger(0);
    	
    	IntStream.range(0, 6)
    		.filter(i -> valuesList.get(i) >= 2)
    		.forEach(item -> {
    			res.set((item*2)+2);
    		});
    	
    	if(res.get()!=0) {
    		return res.get();
    	}
    	return 0;
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
    
    private void buildValue(AtomicInteger atomicInteger, int value, int size) {
		IntStream.range(0, size)
			.filter(i -> dice.get(i) == value)
			.forEach(i -> atomicInteger.getAndAdd(value));
	}
    
    private static int filterAndAddValue(int d1, int d2, int d3, int d4, int d5, int valueTest) {
    	List<Integer> valuesList = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5));
    	AtomicInteger atomicInteger = new AtomicInteger(0);
		valuesList.stream()
			.filter(i-> i == valueTest)
			.forEach(i -> atomicInteger.getAndAdd(valueTest));
        return atomicInteger.get();
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

