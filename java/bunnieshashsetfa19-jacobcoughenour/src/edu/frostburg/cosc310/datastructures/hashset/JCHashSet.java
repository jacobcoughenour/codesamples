package edu.frostburg.cosc310.datastructures.hashset;

public class JCHashSet implements HashSet<String> {

	private static final int INITIAL_CAPACITY = 32;
	private static final float MAX_LOAD_FACTOR = 0.8f;
	private static final int PROBE_POW = 2;
	private static final boolean DEBUG = false;

	/**
	 * https://i.imgur.com/d0xZj3L.jpg
	 **/
	private String[] bigChungus;
	private int currentMaxSize;
	private int _size;

	private int _stats_rehashCount = 0;
	private int _stats_rehashesFromProbing = 0;
	private int _stats_rehashesFromLoadFactor = 0;
	private int _stats_collisions = 0;

	/**
	 * JCHashSet constructor
	 */
	public JCHashSet() {
		this(INITIAL_CAPACITY);
	}

	/**
	 * JCHashSet constructor
	 *
	 * @param size initial table size
	 */
	public JCHashSet(int size) {
		bigChungus = new String[size + (size % 2)];
		_size = 0;
		currentMaxSize = (int) (bigChungus.length * MAX_LOAD_FACTOR);
	}

	/**
	 * Gets the hash for
	 *
	 * @param element
	 * @param length
	 * @return
	 */
	private static int getHash(String element, int length) {
		return Math.abs(element.hashCode()) % length;
	}

	/**
	 * Probe for an empty index for the given element
	 *
	 * @param element element to probe for
	 * @return first empty index or the -index-1 if element exists
	 */
	private int probe(String element) {

		int x = 0;
		int start = JCHashSet.getHash(element, bigChungus.length);
		int index = start;
		int offset = 0;
		int lenOverPow = bigChungus.length / PROBE_POW;

		// while index is not empty
		while (bigChungus[index] != null) {

			// keep track of total collisions
			_stats_collisions++;

			// element already exists
			if (bigChungus[index].equals(element))
				return -index - 1;

			// quadratic probing
			x++;
			index = (start + offset + ((int) Math.pow(x, PROBE_POW))) % bigChungus.length;


			// if the array size is a power-of-two or a triangular number then
			// there is no guarantee that all the empty cells will be reached.
			// So we shift the probing by 1 when x > length/pow until our offset
			// is > len/pow. Then we expand, rehash, and start probing all over
			// again as a last resort.
			if (x > lenOverPow || index < 0) {

				// if we have offset the probing by > len/pow
				// then we try rehashing and reset our probing
				// (this almost never happens)
				if (offset > lenOverPow) {

					// expand and rehash
					if (DEBUG)
						System.out.println("probing is taking too long, time to rehash..");
					_stats_rehashesFromProbing++;
					_expandAndRehash();

					// recalculate our element hash
					index = JCHashSet.getHash(element, bigChungus.length);

					// reset our quadratic probing
					x = 0;
					offset = 0;

				} else {

					// shift probing by 1
					offset++;

					// reset our quadratic probing
					index = start;
					x = 0;
				}

			}

		}

		// return the empty index
		return index;
	}

	/**
	 * Add element to the HashSet
	 *
	 * @param element element to add
	 * @return true if added, false if element already exists
	 */
	@Override
	public boolean add(String element) {

		// check if we need to expand and rehash
		if (_size >= currentMaxSize) {
			_stats_rehashesFromLoadFactor++;
			_expandAndRehash();
		}

		return _add(element);
	}

	/**
	 * Internal add that doesn't check load
	 * This is used for rehashing
	 *
	 * @param element element to add
	 * @return true if added, false if element already exists
	 */
	private boolean _add(String element) {

		int index = probe(element);

		// already exists
		if (index < 0)
			return false;

		// added
		bigChungus[index] = element;
		_size++;
		return true;
	}


	/**
	 * expand internal array by 2x and rehash all the current elements
	 */
	private void _expandAndRehash() {
		String[] oldChungus = bigChungus;

		_size = 0;

		bigChungus = new String[bigChungus.length * 2];

		currentMaxSize = (int) (bigChungus.length * MAX_LOAD_FACTOR);

		for (String s : oldChungus)
			if (s != null)
				if (!_add(s)) {
					if (DEBUG)
						System.out.println("DUPLICATE " + s + " WAS FOUND DURING REHASH");
				}
		_stats_rehashCount++;

		if (DEBUG)
			System.out.println("expanded and rehashed " + _size + " entries");
	}

	@Override
	public boolean remove(String element) {

		int index = probe(element);

		// element exists
		if (index < 0) {
			// remove from array
			bigChungus[-index - 1] = null;

			return true;
		}

		return false;
	}

	@Override
	public boolean find(String element) {
		return probe(element) < 0;
	}

	/**
	 * @return total items in the set
	 */
	@Override
	public int size() {
		return _size;
	}

	/**
	 * @return the internal table size of the hashset
	 */
	@Override
	public int tableSize() {
		return bigChungus.length;
	}

	/**
	 * @return true if the hashset is empty
	 */
	@Override
	public boolean isEmpty() {
		return _size == 0;
	}

	/**
	 * @return returns my name
	 */
	@Override
	public String myName() {
		return "Jacob Coughenour";
	}

	/**
	 * @return stats - total rehash count
	 */
	public int stats_rehashCount() {
		return _stats_rehashCount;
	}

	/**
	 * @return stats - total rehashed triggered during probing
	 */
	public int stats_rehashesFromProbing() {
		return _stats_rehashesFromProbing;
	}

	/**
	 * @return stats - total rehashes triggered by a load factor check
	 */
	public int stats_rehashesFromLoadFactor() {
		return _stats_rehashesFromLoadFactor;
	}

	/**
	 * @return stats - total probe collisions
	 */
	public int stats_collisions() {
		return _stats_collisions;
	}

	/**
	 * @return stats - current load
	 */
	public float stats_currentLoad() {
		return _size / (float) bigChungus.length;
	}

}