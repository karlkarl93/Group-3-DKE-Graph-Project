public class CustomKColouringSearch {
    
    public static void main(String[] args) {
        permute(9, 3);
    }

    protected void permute(int n, int k) {
        // Construct an array whereby indices represent vertices and values represent colours. Initialise with no colour
        int[] colouring = new int[n]; for (int i = 0; i < n; i++) colouring[i] = -1;
        
        // Example for colouring
        colouring = {0, 1, 2, 2, -1, -1, -1, -1, -1};
        
        // Try all combinations and their permutations for vertices and colours
        boolean isFeasible = false; int i = 0;
        while (isFeasible != true && i < n) {
            
            // Loop through all possible colours for this vertex
            int j = colouring[i] +1; colouring[i] = -1;
            while (colouring[i] == -1 && j < k) if (isProperColour(i,j)) colouring[i] = j; else j++;
            
            // If no colour was found for this vertex move to previous vertex, move to the next vertex
            if (colouring[i] == -1) i--; else i++;
            
            // All vertices have been assigned a colour
            if (i == n) isFeasible = true;
        }
    }
}
