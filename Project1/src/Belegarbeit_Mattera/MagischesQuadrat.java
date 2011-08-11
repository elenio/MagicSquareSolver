/** Programm zum Finden magischer 4x4 Quadrate mit Initialwerten
 *   @author Elenio Mattera (# 249955)
 *   @version 0.9  - 20.01.2009
 */


public class MagischesQuadrat {
  private static int zaehler;
	/** Die Konstante "N" bezeichnet die Seitenlänge der Matrix */
	private static int N = 4;
	/** Die Konstante "ziffer" bezeichnet die Zahl, bei der das Backtracking anfangen soll */ 
	private static int ziffer = 1; 
	/** Nimmt einen Timestamp (time1) */
	private static long time1;
	/** hier werden alle verbrauchten Zahlen auf true gesetzt und auf Basis ihres (Indizes-1) geprüft  */
	private static boolean[] benutzteZiffern;

	/** 
	 * Hauptprogramm. Bereitet den Suchvorgang vor, steuert ihn und veranlasst die Ausgabe
	 * @param args literale vom Typ String, die an das Programm übergeben werden.
	 * Sie werden nicht verarbeitet. 
	 */
	public static void main (String args[]) {       
		//erstellt die n*n Matrix  
		int[][] quadrat = new int[N][N];

		quadrat = initialisiere(quadrat);
		time1 = System.currentTimeMillis();
		erzeuge(N, quadrat, ziffer);
		//erzeugeSchnell(N, quadrat, ziffer);
		zeige(quadrat);
	}

	/** 
	 * Die Methode initialisiere() füllt die Matrix mit 0 und den Vorgabewerten
	 * und initialisiert ebenso <i>benutzteZiffern</i> mit false bzw. den Initialwerten mit true.
	 * @param quadrat vom Typ int[][] - ist das zu initialisierende Array
	 * @return gibt das mit 0 (und den Vorgabewerten) initialisierte Array zurück
	 */
	public static int[][] initialisiere(int[][] quadrat){
		for(int x = 0; x < N; x++){
			for(int y = 0; y < N; y++){
				quadrat[x][y] = 0;
			}
		}
		//Vorgabewerte setzen
		quadrat[1][1] = 12;
		quadrat[3][1] = 15;
		quadrat[3][2] = 14; 

		benutzteZiffern = new boolean[N*N];
		for(int i=0; i<benutzteZiffern.length; i++){
			benutzteZiffern[i] = false;
		}
		benutzteZiffern[11] = true;
		benutzteZiffern[13] = true;
		benutzteZiffern[14] = true;

		return quadrat;
	}

	/** 
	 * Die Methode erzeuge() füllt das Quadrat mit Zahlen von 1-16 
	 * Dabei wird durch die Methode <i>checkZiffer()</i> bei jeder Zahl geprüft, ob diese schon im Quadrat vorhanden ist 
	 * Die zellenweise Befüllung des Quadrats wird durch ein rekursives Backtracking vorgenommen,
	 * bei der sich die Methode immer wieder selbst mit verändertem Parameter (ziffer+1) aufruft.
	 * Da hier jeder Wert einzelt überprüft wird, dauert diese Variante in etwa ~15min
	 * 
	 * @param N  Seitenlänge des Quadrats
	 * @param quadrat das zu füllende Quadrat
	 * @param ziffer die einzutragende Ziffer, beim ersten Durchlauf "1"
	 * @return gibt das Magische Quadrat zurück
	 */
  public static int[][] erzeuge(int N, int[][] quadrat, int ziffer){
		if ((ziffer > (N*N)) && isMagisch(quadrat)){
			zeige(quadrat);
			System.exit(0);
			return(quadrat);
		}
		else {
			for (int x = 0;x<N; x++){
				for (int y = 0; y < N; y++){
					if (quadrat[x][y] == 0) {
						if(!checkZiffer(ziffer)){
							// Ziffer einsetzen und als benutzt markieren
							quadrat[x][y] = ziffer;
							benutzteZiffern[ziffer - 1] = true;
							zaehler += 1;
							quadrat = erzeuge(N, quadrat, ziffer+1);   //Backtracking Aufruf           
							
							// Ziffer zurücksetzen und als unbenutzt markieren
							quadrat[x][y] = 0;          
							benutzteZiffern[ziffer - 1] = false;
						} else {
							quadrat = erzeuge(N, quadrat, ziffer+1);	//Backtracking Aufruf 
							zaehler += 1;
            }

					}
				}
			}
		}
		return quadrat;
	} 
	
  /**
	 * Die Methode erzeugeSchnell() füllt das Quadrat mit Zahlen von 1-16 
	 * Die zellenweise Befüllung des Quadrates wird durch ein rekursives Backtracking vorgenommen, 
	 * bei der sich die Methode immer wieder selbst mit verändertem Parameter (ziffer+q) aufruft. 
	 * erzeugeSchnell() verzichtet auf die <i>checkZiffern()</i> Methode, da die Vorgabewerte hier statisch integriert sind
	 * Daher ist sie auch schneller und dauert nur ca. 5 Minuten
	 * 
	 * @param N Seitenlänge des Quadrats
	 * @param quadrat das zu füllende Quadrat 
	 * @param ziffer die einzutragende Ziffer, beim ersten Durchlauf "1" 
	 * @return gibt das Magische Quadrat zurück
	 */
  public static int[][] erzeugeSchnell(int N, int[][] quadrat, int ziffer){
		if ((ziffer > (N*N)) && isMagisch(quadrat)){
			zeige(quadrat);
			System.exit(0);
		}
		else {
			for (int x = 0;x<N; x++){
				for (int y = 0; y < N; y++){
					if (quadrat[x][y] == 0) {
					 
						quadrat[x][y] = ziffer;     // Ziffer einsetzen      
						if (ziffer==11) { // Sonderfall abfangen, Initialwert darf nicht überschrieben werden
							quadrat = erzeugeSchnell(N, quadrat, ziffer+2);	// Backtracking Aufruf

            } else {
							if (ziffer==13){	 // Sonderfall abfangen, Initialwert darf nicht überschrieben werden
								quadrat = erzeugeSchnell(N, quadrat, ziffer+3);	 // Backtracking Aufruf

              } else {
								quadrat = erzeugeSchnell(N, quadrat, ziffer+1);	// Backtracking Aufruf

              }
						}
						quadrat[x][y] = 0;		//Ziffer zurücksetzen
					}
				}
			}
		}
		return quadrat;
	}

	/** 
	 * checkZiffer() überprüft die aktuelle Zahl auf ihre Einmaligkeit 
	 * durch den Vergleich des Indizes von benutzteZiffern[]
	 * @param ziffer vom Typ int ist die zu überprüfende Zahl
	 * @return der Returnwert ist der Zelleninhalt; true, falls die Zahl bereits vorhanden ist
	 */
  public static boolean checkZiffer(int ziffer ){
		return benutzteZiffern[ziffer-1];
	} 

  /**
	 * isMagisch() überprüft die Matrix auf ihre magischen Eigenschaften. Sprich die Zeilen-, Spalte- und Diagonalsummen
	 * @param m vom Typ int[][] ist das komplettbefüllte MagischeQuadrat
	 * @return gibt true zurück, wenn alle Summen korrekt sind und die Matrix somit magisch ist. 
	 */
	public static boolean isMagisch(int [][] m){
		int s=0, t=0;

		//1. Diagonale - Hauptdiagonale
		for(int x=0; x < N; x++){
			s+=m[x][x];
		}

		//2. Diagonale - Nebendiagonale
		for(int x=0; x < N; x++){
			t+=m[N-x-1][x];
		}
		if (t != s) return false;
		
		//Zeilen
		for (int y = 0; y < N; y++) {
			int k = 0;
			for(int x = 0; x < N; x++){
				k += m[y][x];
			}
			if (k != s) return false;
		}
		
		//Spalten
		for (int x = 0; x < N; x++){
			int k=0;
			for(int y = 0; y < N; y++){
				k += m[y][x];
			}
			if (k != s) return false;
		}
		return true;
	}

	/** 
	 * zeige() dient der Darstellung des Magisches Quadrats und gibt jede Zelle aus 
	 * In 2 for-Schleifen durchläuft es zeilenweise das Array und gibt jeden Wert mit einem Platzhalter von 3 Zeichen aus
	 * Am Schluß wird noch ein 2. Timestamp (time2) generiert und somit die Dauer der Berechnung in Millisekunden ausgegeben.
	 * @param quadrat vom Typ int[][] ist das Magische Quadrat. 
	 */ 
	public static void zeige(int[][] quadrat){
		System.out.println("------------");
		for (int y = 0; y < N; y++){
			for (int x = 0; x < N; x++){
				System.out.printf("%3s", quadrat[y][x] + "");
			}
			System.out.println();
		}
		System.out.println("------------");
		long time2 = System.currentTimeMillis();
		System.out.println(isMagisch(quadrat) + " - " + (time2-time1) + "ms " +  zaehler); 
        }
}