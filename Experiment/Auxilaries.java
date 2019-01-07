class Auxilaries {
    
    // MARK: Methods
    
    protected static double[][] matrixMultiply(double[][] matrixA, double[][] matrixB) {
        
        double[][] matrixC = new double[matrixA.length][matrixB[0].length];
        
        for (int rowA=0; rowA < matrixA.length; rowA++) {
            double runningTotal = 0;
            for (int columnB = 0; columnB < matrixB[0].length; columnB++) {
                for (int entry = 0; entry < matrixA[0].length; entry ++) {
                    runningTotal += matrixA[rowA][entry] * matrixB[entry][columnB];
                }
                matrixC[rowA][columnB] = runningTotal;
                runningTotal = 0;
            }
        }
        return matrixC;
    }
    
    protected static int[] insertIntAtFirstIndex(int[] oldArray, int element) {
        int[] newArray = new int[oldArray.length + 1];
        newArray[0] = element;
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i+1] = oldArray[i];
        }
        
        return newArray;
    }
    
    // Returns the indices sorted by the magnitude of the corresponding values
    protected static int[] sortIndicesByValueInDescendingOrder(int[] oldArray) {
        int[] newArray = new int[oldArray.length];
        for (int i = 0; i < oldArray.length; i++) {
            // Find the index of the highest value
            int maximumValue = 0;
            int maximumIndex = 0;
            for (int j = 0; j < oldArray.length; j++) {
                if (maximumValue < oldArray[j]) {
                    maximumValue = oldArray[j];
                    maximumIndex = j;
                }
            }
            newArray[i] = maximumIndex;
            oldArray[maximumIndex] = 0;
        }
        return newArray;
    }
    
    protected static int[] removeZeros(int[] oldArray) {
        int[] newArray = new int[0];
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != 0) newArray = appendInt(newArray, oldArray[i]);
        }
        return newArray;
    }
    
    private static int findMaximumIndex(int[] array) {
        int maximum = 0;
        int maximumIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maximum) {
                maximum = array[i];
                maximumIndex = i;
            }
        }
        return maximumIndex;
    }
    
    protected static boolean containsInt(int[] array, int element) {
        boolean foundInt = false; int i = 0;
        while (!foundInt && i < array.length) {
            if (array[i] == element) foundInt = true;
            i++;
        }
        return foundInt;
    }
    
    
    /**
     This method adds an integer to an array of integers.
     @param oldArray the array of integers that should be extended
     @param element the element that should be appended to the oldArray
     @return a new array containing the appended element
     */
    protected static int[] appendInt(int[] oldArray, int element) {
        int[] newArray = new int[oldArray.length+1];
        
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        
        newArray[oldArray.length] = element;
        
        return newArray;
    }
    
    protected static int[] removeIntAtIndex(int[] oldArray, int index) {
        int[] newArray = new int[oldArray.length - 1];
        for (int i = 0; i < oldArray.length; i++) {
            if (i < index) newArray[i] = oldArray[i];
            else if (i > index) newArray[i - 1] = oldArray[i];
        }
        return newArray;
    }
    
    /**
     This method removes the element passed as parameter from the array passed as parameter. The method can be used in any context where an element of an array needs to be removed. When the element to be removed is not listed in the array, the old array will be returned
     @param element the element to be removed
     @param oldArray the array on which the operation should be performed
     @return returns a new array that is a copy of the old array minus the element that has to be removed or the old array if the element to be removed was not listed in that array
     */
    protected static int[] removeInt(int[] oldArray, int element) {
        // Ensure that the element to be removed is listed in the old array
        boolean elementIsListed = false;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] == element) elementIsListed = true;
        }
        
        // Iterate over the old array and copy all elements but the element to be removed
        int[] newArray = new int[oldArray.length - 1];
        if (elementIsListed) {
            
            int iterated = 0;
            int copied = 0;
            
            while (iterated < oldArray.length) {
                if (oldArray[iterated] != element) {
                    newArray[copied] = oldArray[iterated];
                    copied ++;
                }
                iterated ++;
            }
        }
        
        // return the result
        return newArray;
    }
}

