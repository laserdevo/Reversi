/**
 * Implémentation d'un jeu de Reversi
 * @author B.L
 */

class Reversi{

	void principal(){
		testCopierTableau();
		System.out.println();
		jouer();
	}

	/**
	* Affiche le plateau de jeu
	*  @param couleurs un tableau de char (couleurs[0] = plateau, couleurs[1] = pions x, couleurs[2] = pions o, couleurs[3] = reset/blanc)
	*/
	void afficherPlateau(char [][] jeu, String[] couleurs){
		// Ligne avec les chiffre des colonne
		System.out.print(couleurs[0]+"     ");
		for(int i = 0; i < jeu.length; i++){

			if( i < 10){
				System.out.print(i+"   ");
			}else{
				System.out.print(i+"  ");
			}
		}

		System.out.println();

		for(int i = 0; i < jeu.length; i++){
			
			// Numéro ligne
			if(i < 10){
				System.out.print(i +"  | ");
			}else{
				System.out.print(i +" | ");
			}
			
			// contenu du plateau
			for(int j = 0; j < jeu[i].length; j++){
				if(jeu[i][j] == ('\0')){
					System.out.print(" " + " | ");
				}else if(jeu[i][j] == 'x'){
					System.out.print(couleurs[1]+jeu[i][j]+couleurs[0] + " | ");
				}else if(jeu[i][j] == 'o'){
					System.out.print(couleurs[2]+jeu[i][j]+couleurs[0] + " | ");
				}else{
					System.out.print(jeu[i][j]+ " | ");
				}
			}

			System.out.println();
		}

		System.out.println(couleurs[3]);
	}

	/**
	* Demande au joueur le nombre de ligne du plateau
	* @return le nombre de ligne
	*/
	int choixNbLigne(){
		int nbLigne;
		System.out.println("Choix du nombre de ligne (chiffre pair entre 4 et 16) :");

		do{
			nbLigne = SimpleInput.getInt("Entrer le nombre de ligne : ");
		} while (nbLigne % 2 != 0 || nbLigne < 4 || nbLigne > 16);

		System.out.println();

		return nbLigne;
	}

	/**
	* Demande au joueur le mode de jeu
	* @return  tableau modeDiff, le mode de jeu choisi et la difficulté
	*/
	String[] choixMode(){
		String mode;
		String difficulte = "nul";
		
		//Choix du mode de jeu
		System.out.println("Choix du mode de jeu :");

		do{
			mode = SimpleInput.getString("Taper 'solo' ou 'duo' : ");
		}while(!(mode.equals("solo") || mode.equals("duo")));
		
		//Si le mode de jeu en "solo", choix de la difficulté;
		if(mode.equals("solo")){
			System.out.println("Choix du mode de jeu :");
			do{
				difficulte = SimpleInput.getString("Taper 'normal' ou 'difficile' : ");
			}while(!(difficulte.equals("normal") || difficulte.equals("difficile")));
		}

		System.out.println();
		
		String[] modeDiff ={mode,difficulte};  

		return modeDiff;
	}

	/**
	* Efface le texte sur l'écran de la console
	*/
	void clearConsole(){
		// Efface tout l’écran et place le curseur en haut à gauche,  [H repositionne le curseur, [2J efface le contenu
		System.out.print("\033[H\033[2J");
		System.out.flush(); // force l’envoi immédiat
	}

	/**
	* Demande au la couleur du plateau et des pions
	* @return un tableau de string contenant les code ANSI pour les couleurs
	*/
	String[] choixCouleur(){
		int [] couleursInt = new int [3];
		String [] couleursString = new String [4];
		String [] couleursDispo = {"\u001B[31m","\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m","\u001B[37m"};

		System.out.println("Couleurs disponibles :");
		System.out.println("\t Rouge taper 1");
		System.out.println("\t Vert taper 2");
		System.out.println("\t Jaune taper 3");
		System.out.println("\t Bleu taper 4");
		System.out.println("\t Magenta taper 5");
		System.out.println("\t Cyan taper 6");
		System.out.println("\t Blanc (par defaut) taper 7");
		System.out.println();

		for(int i = 0; i < couleursInt.length; i++){

			if( i == 0){
				System.out.println("Choissisez la couleur du plateau : ");
			}else if( i == 1){
				System.out.println("Choissisez la couleur des pions x : ");
			}else{
				System.out.println("Choissisez la couleur des pions o : ");
			}

			do{
				couleursInt[i] = SimpleInput.getInt("Nombre de votre couleur : ");
				if (couleursInt[i] < 1 || couleursInt[i] > 7) {
					System.out.println("Veuillez entrer un nombre entre 1 et 7.");
				}
			}while(couleursInt[i] < 1 || couleursInt[i] > 7);

			couleursString[i] = couleursDispo[couleursInt[i]-1];
		}

		couleursString[3] = "\u001B[37m";

		System.out.println();

		return couleursString;
	}


	/**
	* Initialise le tableau avec les pionts de départs
	* @return  jeu la référence du  tableau 2d
	*/
	char [][] initialiserTableau () {
		int nbLigne = choixNbLigne ();
		//Initialiser le plateau
		char [][] jeu = new char [nbLigne][nbLigne];

		int nbLigneDivDeux = nbLigne / 2;

		//Initialiser le pions de départs
		jeu[nbLigneDivDeux][nbLigneDivDeux] = 'x';
		jeu[nbLigneDivDeux - 1][nbLigneDivDeux - 1] = 'x';
		jeu[nbLigneDivDeux - 1][nbLigneDivDeux] = 'o';
		jeu[nbLigneDivDeux][nbLigneDivDeux - 1] = 'o';


		return jeu;
	}

	/**
	* Afficher les positions valides dans le tableau
	* @param jeu le tableau dans lequel on veut afficher les positions valides
	* @param joueurActif le joueur dont le tour est en cours
	*/
	void initialiserPositionsValides(char [][] jeu, char joueurActif) {
		for(int i = 0; i < jeu.length; i++){
			for(int j = 0; j < jeu[i].length; j++){
				if (estPositionValide(jeu,i , j, joueurActif)) {
					jeu[i][j] = '.';
				}
			}
		}
	}

	/**
	* Nettoie les positions valide du tour précédent
	* @param jeu le tableau dans lequel on veut effacer les positions valides du tour précédent
	*/
	void nettoyerPositionsValides(char[][] jeu) {
		for (int i = 0; i < jeu.length; i++) {
			for (int j = 0; j < jeu[i].length; j++) {
				if (jeu[i][j] == '.') {
					jeu[i][j] = '\0';
				}
			}
		}
	}


	/**
	* Place le pion du joueur sur la positions valide qu'il souhaite
	* @param jeu le tableau dans lequel on veut afficher les positions valides
	* @param joueurActif le joueur dont le tour est en cours
	*/
	void placerPionHumain (char [][] jeu, char joueurActif){
		System.out.println ("Au tour du joueur " + joueurActif);
		boolean positionValide;
		int ligne;
		int colonne;
		
		// Choix ligne et colonne
		do{
			do{
				ligne = SimpleInput.getInt ("Sur quelle ligne souhaitez vous placer votre pion ? : ");
				colonne = SimpleInput.getInt ("Sur quelle colone souhaitez vous placer votre pion ? : ");
			}while(!(ligne < jeu.length && colonne < jeu.length));

			positionValide = estPositionValide (jeu, ligne, colonne, joueurActif);
		}while (!positionValide);

		jeu[ligne][colonne] = joueurActif;

		retournerPions(jeu, ligne, colonne, joueurActif);
	}

	/**
	* Retourne les pions adverse après avoir posé un pion
	* @param jeu le tableau dans lequel on veut retourner les pions
	* @param ligne la ligne correspondant à la position du pion qu'on à posé
	* @param colonne la colonne correspondant à la position du pion qu'on à posé
	* @param joueurActif le joueur dont le tour est en cours
	*/
	void retournerPions(char [][] jeu, int ligne, int colonne, char joueurActif){

		// Tableau temporaire pour stocker les positions à retourner
		int[][] pionsTemp = new int[jeu.length * jeu[0].length][2];

		int x; // ligne de la case à vérifié
		int y; // colonne de la case à vérifié
		boolean trouveAdversaire;
		boolean continuer;
		int compteur;

		char joueurAdverse;
		if (joueurActif == 'x') {
			joueurAdverse = 'o';
		} else {
			joueurAdverse = 'x';
		}

		// On parcours toute les directions
		for (int colonneDir = -1; colonneDir <= 1; colonneDir++) {
			for (int ligneDir = -1; ligneDir <= 1; ligneDir++) {

				// On touche pas la case ou l'on veut poser le pion
				if (colonneDir != 0 || ligneDir != 0) {
					x = ligne + ligneDir;
					y = colonne + colonneDir;

					trouveAdversaire = false;
					continuer = true;
					compteur = 0;

					// On dépasse pas les limites du plateau
					while (continuer && y >= 0 && y < jeu[0].length && x >= 0 && x < jeu.length) {

						// Si on trouve un pions adverse, on met ses cordonées dans un tableau
						if (jeu[x][y] == joueurAdverse) {
							trouveAdversaire = true;
							pionsTemp[compteur][0] = x;
							pionsTemp[compteur][1] = y;
							compteur++;

							x += ligneDir;
							y += colonneDir;

						// Si on trouve le joueur actif et qu'on à trouvé un adversaire on retourne les pions du tableau temporaire
						} else if (jeu[x][y] == joueurActif && trouveAdversaire) {
							for (int i = 0; i < compteur; i++) {
								jeu[pionsTemp[i][0]][pionsTemp[i][1]] = joueurActif;
							}
							continuer = false;

						} else {
							continuer = false;
						}
					}
				}
			}
		}
	}

	/**
	 * Vérifie si une position est valide où non
	 * @param jeu le tableau dans lequel on veut vérifier la position valide
	 * @param ligne la ligne correspondant à la position qu'on veut vérifier
	 * @param colonne la colonne correspondant à la position qu'on veut vérifier
	 * @param joueurActif le joueur dont le tour est en cours
	 * @return true si la position est valide, false sinon
	 */
	boolean estPositionValide(char [][] jeu, int ligne, int colonne, char joueurActif){
		boolean positionValide = false; //un seul booléen de sortie;
		char joueurAdverse = '\0';
		int x; // ligne de la case à vérifié
		int y; // colonne de la case à vérifié
		boolean trouveAdversaire;
		boolean continuer;

		if (joueurActif == 'x') {
			joueurAdverse = 'o';
		} else {
			joueurAdverse = 'x';
		}

		// Si la case n'est pas vide, elle ne peut pas être une position valide
		if (jeu[ligne][colonne] == '\0' || jeu[ligne][colonne] == '.') {

			// Parcours des 8 directions autour de la case
			for (int colonneDir = -1; colonneDir <= 1; colonneDir++) {
				for (int ligneDir = -1; ligneDir <= 1; ligneDir++) {

					// On regarde pas la case ou l'on veut poser le pion
					if (colonneDir != 0 || ligneDir != 0) {

						// case autour
						x = ligne + ligneDir;
						y = colonne + colonneDir;

						trouveAdversaire = false;
						continuer = true;

						// Vérifier qu'on n'est pas hors du plateau
						if (y >= 0 && y < jeu[0].length && x >= 0 && x < jeu.length) {

							// Il doit y avoir au moins un pion adverse à côté
							if (jeu[x][y] == joueurAdverse) {
								trouveAdversaire = true;

								// On avance d'une case dans la direction
								x += ligneDir;
								y += colonneDir;

								// On dépasse pas les limites du plateau
								while (continuer && x >= 0 && x < jeu[0].length && y >= 0 && y < jeu.length) {

									// Si on trouve le joueur actif et qu'on à trouvé l'adversaire
									if (jeu[x][y] == joueurActif && trouveAdversaire) {
										positionValide = true;
										continuer = false;

									} else if (jeu[x][y] == joueurAdverse) {
										x += ligneDir;
										y += colonneDir;
									} else {
										continuer = false;
									}
								}
							}
						}
					}
				}
			}
		}
		return positionValide;
	}

	/**
	* Affiche le nom du jeu en art ASCII
	*/
	void afficherTitre() {
		System.out.print("\u001B[35m");
		System.out.println(" /$$$$$$$  /$$$$$$$$ /$$    /$$ /$$$$$$$$ /$$$$$$$   /$$$$$$  /$$$$$$");
		System.out.println("| $$__  $$| $$_____/| $$   | $$| $$_____/| $$__  $$ /$$__  $$|_  $$_/");
		System.out.println("| $$  \\ $$| $$      | $$   | $$| $$      | $$  \\ $$| $$  \\__/  | $$  ");
		System.out.println("| $$$$$$$/| $$$$$   |  $$ / $$/| $$$$$   | $$$$$$$/|  $$$$$$   | $$  ");
		System.out.println("| $$__  $$| $$__/    \\  $$ $$/ | $$__/   | $$__  $$ \\____  $$  | $$  ");
		System.out.println("| $$  \\ $$| $$        \\  $$$/  | $$      | $$  \\ $$ /$$  \\ $$  | $$  ");
		System.out.println("| $$  | $$| $$$$$$$$   \\  $/   | $$$$$$$$| $$  | $$|  $$$$$$/ /$$$$$$");
		System.out.println("|__/  |__/|________/    \\_/    |________/|__/  |__/ \\______/ |______/");
        System.out.println("\u001B[0m");
        System.out.println();
        // \u001B[32m met le texte en vert (code ANSI)
        // \u001B[0m rénitialise la couleur
	}

	/**
	* Change le joueur courant
	* @param joueurInitial un caractère représentant le joueur : x ou o
	* @return si le joueur ’x’ est en parametre alors renvoie ’o’
	* sinon renvoie ’x’
	*/
	char changeJoueur(char joueurInitial){
		if(joueurInitial == 'x'){
			joueurInitial = 'o';
		}else{
			joueurInitial = 'x';
		}

		return joueurInitial;
	}

	/**
	* Verifie si toutes les cases du plateau sont remplies
	* (différentes de ’ ’)
	* @param plateau le plateau de jeu
	* @return true si tout le plateau est rempli, false sinon.
	*/
	boolean estRempli(char[][] plateau){
		boolean res = true;

		for(int i = 0; i < plateau.length; i++){
			for(int j = 0; j < plateau[i].length; j++){
				if(plateau[i][j] != 'x' || plateau[i][j] != 'o'){
					res = false;
				}
			}
		}

		return res;
	}

	/**
	 * Créer une copie d'un tableau (le tableau doit avoir autant de colonnes que de lignes)
	 * @param jeu le tableau que l'on veut copier
	 * @return la copie de notre tanbleau
	 */
	char[][] copierTableau (char[][] jeu) {
		char[][] copie = new char [jeu.length][jeu.length];
		for (int i = 0; i < jeu.length; i++) {
			for (int j = 0; j < jeu[i].length; j++) {
				copie[i][j] = jeu [i][j];
			}
		}
		return copie;
	}

	/**
	 * trouver le coup pour lequel le bot retourne le plus de pions
	 * @param jeu le plateau de jeu sur lequel on joue
	 * @param bot le signe des pions que joue le bot
	 */
	void botMeilleurCoup (char[][] jeu, char bot) {
		//Intitialisation
		int pionsBotApresMeilleurCoup = 0;
		int pionsBotApresCoup = 0;
		int meilleureLigne = 0;
		int meilleureColonne = 0;
		
		//On parcours le tableau jusqu'à ce que l'on trouve une position valide
		for (int i = 0; i < jeu.length; i++) {
			for (int j = 0; j < jeu[i].length; j++) {
				
				if (estPositionValide(jeu, i, j, bot)) {
					
					// Crée une nouvelle copie du tableau à chaque coup testé
					char[][] copie = copierTableau(jeu);

					
					//Joue dans une copie du tableau chacune des positions valides
					copie[i][j] = bot;
					retournerPions (copie, i, j, bot);
					pionsBotApresCoup = 0;
					
					//Une fois le coup joué, vérifie dans le tableau copier le nombre de pions appartenants au bot
					for (int k = 0; k < copie.length; k++) {
						for (int h = 0; h < copie[k].length; h++) {
							if (copie[k][h] == bot) {
								pionsBotApresCoup += 1;
							}
						}
					}
					
					//Enregistre le coup après lequel le bot possède le plus de pions sur le terrain
					if (pionsBotApresCoup > pionsBotApresMeilleurCoup) {
						pionsBotApresMeilleurCoup = pionsBotApresCoup;
						meilleureLigne = i;
						meilleureColonne = j;
					}
				}
			}
		}
		//Joue le coup sur le vrai tableau
		jeu[meilleureLigne][meilleureColonne] = bot;
		retournerPions(jeu, meilleureLigne, meilleureColonne, bot);
	}

	/**
	 * Fait jouer un coup aleatoire au bot parmis les positions valides
	 * @param jeu le plateau sur lequel on joue
	 * @param bot le signe corréspondant aux pions que le bot joue
	 */
	void botAleatoire (char[][] jeu, char bot) {
		int ligneAleatoire;
		int colonneAleatoire;
		//Tirer des lignes et colonnes aleatoires jusqu'à ce que la position soit valide
		do{
			ligneAleatoire = (int) (Math.random() * jeu.length);
			colonneAleatoire = (int) (Math.random() * jeu.length);
		}while (!estPositionValide (jeu, ligneAleatoire, colonneAleatoire, bot));
		//Jouer le coup de la position aleatoire
		jeu[ligneAleatoire][colonneAleatoire] = bot;
		retournerPions (jeu, ligneAleatoire, colonneAleatoire, bot);
	}

	/**
	 * Vérifie si un joueur a des positions valides lui permetant de jouer
	 * @param jeu le tableau du jeu dans lequel on veut vérifier si le joueur peut jouer
	 * @param joueur le joueur dont on veut vérifier la possibilité de jouer
	 * @return true si le joueur peut jouer, false sinon
	 */
	boolean peutJouer (char[][] jeu, char joueur) {
		boolean estCapableDeJouer = false;
		int i = 0;
		int j = 0;
		while (i < jeu.length && !estCapableDeJouer) {
			j = 0;
			while (j < jeu[i].length && !estCapableDeJouer) {
				if (estPositionValide(jeu, i, j, joueur) == true) {
					estCapableDeJouer = true;
				}
				j++;
			}
			i++;
		}
		return estCapableDeJouer;
	}

	/**
	* Affiche le score et le gagnant
	* @param jeu le plateau
	* @param couleurs
	*/
	void calculEtAffichageScoreGagnant(char [][] jeu, String[] couleurs){
		int scoreX = 0;
		int scoreO = 0;
		String gagnant = "";

		for(int i = 0; i < jeu.length; i++){
			for(int j = 0; j < jeu[i].length; j++){
				if(jeu[i][j] == 'x'){
					scoreX ++;
				}else if(jeu[i][j] == 'o'){
					scoreO ++;
				}
			}
		}

		if(scoreX > scoreO){
			gagnant = "x";
		}else if(scoreO > scoreX){
			gagnant = "o";
		}else{
			gagnant = "nul";
		}

		afficherScoreGagnant(couleurs, gagnant, scoreX, scoreO);
	}

	/**
	* Affiche le score et le gagnant
	* @param couleurs
	* @param gagnant le gagnant ou nul
	* @param scoreX le score du joueur x
	* @param scoreO le score du joueur o
	*/
	void afficherScoreGagnant(String[] couleurs, String gagnant,  int scoreX, int scoreO){
		clearConsole();

		if(gagnant == "x"){
			System.out.println(couleurs[1]);
			System.out.println("  /$$$$$$                                                      /$$                     /$$   /$$	");
			System.out.println(" /$$__  $$                                                    | $$                    | $$  / $$ ");
			System.out.println("| $$  \\__/  /$$$$$$   /$$$$$$  /$$$$$$$   /$$$$$$  /$$$$$$$  /$$$$$$         /$$      |  $$/ $$/ ");
			System.out.println("| $$ /$$$$ |____  $$ /$$__  $$| $$__  $$ |____  $$| $$__  $$|_  $$_/        |__/       \\  $$$$/  ");
			System.out.println("| $$|_  $$  /$$$$$$$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\ $$  | $$                      >$$  $$  ");
			System.out.println("| $$  \\ $$ /$$__  $$| $$  | $$| $$  | $$ /$$__  $$| $$  | $$  | $$ /$$       /$$       /$$/\\  $$ ");
			System.out.println("|  $$$$$$/|  $$$$$$$|  $$$$$$$| $$  | $$|  $$$$$$$| $$  | $$  |  $$$$/      |__/      | $$  \\ $$ ");
			System.out.println(" \\______/  \\_______/ \\____  $$|__/  |__/ \\_______/|__/  |__/   \\___/                  |__/  |__/ ");
			System.out.println("		     /$$  \\ $$ "  );
			System.out.println("		    |  $$$$$$/"   );
			System.out.println("		     \\______/ "   );
			System.out.println(couleurs[3]);
			System.out.println("Score de x : "+ scoreX);
			System.out.println("Score de o : "+ scoreO);
		}else if(gagnant == "o"){
			System.out.println(couleurs[2]);
			System.out.println("  /$$$$$$                                                      /$$                      /$$$$$$ ");
			System.out.println(" /$$__  $$                                                    | $$                     /$$__  $$");
			System.out.println("| $$  \\__/  /$$$$$$   /$$$$$$  /$$$$$$$   /$$$$$$  /$$$$$$$  /$$$$$$         /$$      | $$  \\ $$");
			System.out.println("| $$ /$$$$ |____  $$ /$$__  $$| $$__  $$ |____  $$| $$__  $$|_  $$_/        |__/      | $$  | $$");
			System.out.println("| $$|_  $$  /$$$$$$$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\ $$  | $$                    | $$  | $$");
			System.out.println("| $$  \\ $$ /$$__  $$| $$  | $$| $$  | $$ /$$__  $$| $$  | $$  | $$ /$$       /$$      | $$  | $$");
			System.out.println("|  $$$$$$/|  $$$$$$$|  $$$$$$$| $$  | $$|  $$$$$$$| $$  | $$  |  $$$$/      |__/      |  $$$$$$/");
			System.out.println(" \\______/  \\_______/ \\____  $$|__/  |__/ \\_______/|__/  |__/   \\___/                   \\______/ ");
			System.out.println("		     /$$  \\ $$ "  );
			System.out.println("		    |  $$$$$$/"   );
			System.out.println("		     \\______/ "   );
			System.out.println(couleurs[3]);
			System.out.println("Score de x : "+ scoreX);
			System.out.println("Score de o : "+ scoreO);
		}else if(gagnant == "nul"){
			System.out.println(couleurs[0]);
			System.out.println(" /$$      /$$             /$$               /$$             /$$   /$$ /$$   /$$ /$$");
			System.out.println("| $$$    /$$$            | $$              | $$            | $$$ | $$| $$  | $$| $$ ");
			System.out.println("| $$$$  /$$$$  /$$$$$$  /$$$$$$    /$$$$$$$| $$$$$$$       | $$$$| $$| $$  | $$| $$ ");
			System.out.println("| $$ $$/$$ $$ |____  $$|_  $$_/   /$$_____/| $$__  $$      | $$ $$ $$| $$  | $$| $$ ");
			System.out.println("| $$  $$$| $$  /$$$$$$$  | $$    | $$      | $$  \\ $$      | $$  $$$$| $$  | $$| $$ ");
			System.out.println("| $$\\  $ | $$ /$$__  $$  | $$ /$$| $$      | $$  | $$      | $$\\  $$$| $$  | $$| $$ ");
			System.out.println("| $$ \\/  | $$|  $$$$$$$  |  $$$$/|  $$$$$$$| $$  | $$      | $$ \\  $$|  $$$$$$/| $$$$$$$$");
			System.out.println("|__/     |__/ \\_______/   \\___/   \\_______/|__/  |__/      |__/  \\__/ \\______/ |________/ ");
			System.out.println(couleurs[3]);
			System.out.println("Score de x : "+ scoreX);
			System.out.println("Score de o : "+ scoreO);
		}
	}

	/**
	* Lance une partie de Reversi
	*/
	void jouer(){

		afficherTitre();
		//Choix des couleurs
		String [] couleurs = choixCouleur();
		//Choix du mode
		String[] mode = choixMode();
		//Création du plateau
		char [][] jeu = initialiserTableau ();

		//Pions de départ
		char joueurActuel = 'o';
		
		// Si mode solo, choix si joueur commence ou pas 
		char bot = 'x';
		String reponse = "";
		
		if (mode[0].equals("solo")) {
			do{
				reponse = SimpleInput.getString("Souhaitez-vous commencer ? (oui/non) : ");
			}while (!reponse.equals("oui") && !reponse.equals("non"));
			
			if(reponse.equals("non")){
				bot = 'o';
			}
		}


		//La fonction qui définie qu'au moins un des deux joueurs peut jouer
		boolean joueursPeutJouer = true;

		//Boucle de jeu : elle continue tant que l’une des conditions de fin n’est pas atteinte
		while(!estRempli(jeu) && joueursPeutJouer) {
			//Efface la console
			clearConsole();

			//Nettoyer les positions valides du tour précédent
			nettoyerPositionsValides(jeu);
			//Initialiser les positions valides
			initialiserPositionsValides(jeu, joueurActuel);

			//Afficher plateau
			afficherPlateau(jeu, couleurs);

			/**
			 * Vérifie si le joueur peut jouer, le fait jouer si c'est le cas
			 * passe son tour sinon et test si l'autre joueur peut jouer.
			 * Si l'autre joueur peut jouer, on continue en changeant de joueur
			 * sinon on arrête le jeu.
			 */
			if (peutJouer(jeu, joueurActuel) == true) {
				//placer un pion
				if (joueurActuel == bot && mode[0].equals("solo")){
					// Bot en mode normal
					if(mode[1].equals("normal")){
						botAleatoire(jeu, joueurActuel);
					// Bot en mode difficile
					}else if(mode[1].equals("difficile")){
						botMeilleurCoup(jeu, joueurActuel);
					}
				// Le joueur joue
				}else{
					placerPionHumain(jeu, joueurActuel);
				}
				
			} else if (peutJouer(jeu, changeJoueur(joueurActuel)) == false){
				joueursPeutJouer = false;
			}

			//Changement de joueur
			joueurActuel = changeJoueur(joueurActuel);

		}

		// Affichage du tableau final
		afficherPlateau(jeu, couleurs);

		//Calcul score et Gagnant et affichage
		calculEtAffichageScoreGagnant(jeu, couleurs);

	}
	
	//-------------- Test fonction copierTableau() --------------------//
	
	/**
	 * Teste la méthode copierTableau()
	 */
	void testCopierTableau() {
		System.out.println();
		System.out.println("*** copierTableau()");

		// Cas 1 : tableau simple
		char[][] tab1 = {{'a','b','c'},{'d','e','f'},{'g','h','i'}};
		char[][] resultat1 = {{'a','b','c'},{'d','e','f'},{'g','h','i'}};
		testCasCopierTableau(tab1, resultat1);

		// Cas 2 : tableau vide
		char[][] tab2 = {};
		char[][] resultat2 = {};
		testCasCopierTableau(tab2, resultat2);
		
		
		// Cas 3 : tableau 1x1
		char[][] tab3 = {{'x'}};
		char[][] resultat3 = {{'x'}};
		testCasCopierTableau(tab3, resultat3);
		
		
		// Cas 4 : tableau 4x4 avec des caractères spéciaux
		char[][] tab5 = {
			{'@','#','$','%'},
			{'A','B','C','D'},
			{'1','2','3','4'},
			{'x','y','z','w'}
		};
		char[][] resultat4 = {
			{'@','#','$','%'},
			{'A','B','C','D'},
			{'1','2','3','4'},
			{'x','y','z','w'}
		};
		testCasCopierTableau(tab5, resultat4);


	}
	/**
	 * Teste un appel de copierTableau
	 * @param tab le tableau à copier
	 * @param resultat le tableau attendu (copie correcte)
	 */
	void testCasCopierTableau(char[][] tab, char[][] resultat) {
		// Affichage
		System.out.print("copierTableau( " + displayTab(tab) + " ) = " + displayTab(resultat) + " : ");
		// Appel
		char[][] tabCopie = copierTableau(tab);
		
		// Verification
		boolean ok = true;

		if (tabCopie.length != resultat.length) {
			ok = false;
		}

		for (int i = 0; i < resultat.length; i++) {
			if (tabCopie[i].length != resultat[i].length) {
				ok = false;
			}
			for (int j = 0; j < resultat[i].length; j++) {
				if (tabCopie[i][j] != resultat[i][j]) {
					ok = false;
				}
			}
		}

		if (ok) {
			System.out.println("OK");
		} else {
			System.out.println("ERREUR");
		}
	}
	
	/**
	 * affiche le tableau
	 * @param tab le tableau à afficher
	 */
	String displayTab(char [][] tab){
		String res = "";
		res +="{";
		
		for(int i = 0; i < tab.length; i++){
			res +="{";
			
			for(int j = 0; j < tab[i].length; j++){
				res += "'" + tab[i][j] + "'";
				if(j < tab[i].length - 1){
					res +=",";
				}
			}
			
			res +="}";
			if(i < tab.length - 1){
				res +=",";
			}
		}
		
		res +="}";
		
		return res;
	}
}

