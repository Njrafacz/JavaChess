package com.javachess.modele.plateau;

import com.javachess.exceptions.GameException;
import com.javachess.helpers.Couleur;
import com.javachess.helpers.Coup;
import com.javachess.helpers.PositionConverter;
import com.javachess.helpers.Sens;
import com.javachess.modele.pieces.Cavalier;
import com.javachess.modele.pieces.Fou;
import com.javachess.modele.pieces.Piece;
import com.javachess.modele.pieces.Pion;
import com.javachess.modele.pieces.Reine;
import com.javachess.modele.pieces.Roi;
import com.javachess.modele.pieces.Tour;

/**
 * Classe qui mod�lise un plateau d'echec
 * 
 * @author Ouzned
 * 
 */
public class Echiquier {

	private Piece[] echiquier = new Piece[64];
	private Coup dernierCoup = null;

	public Echiquier() {
		super();
		initialisePlateau();
	}

	/**
	 * Cr�e les 64 cases du plateau
	 */
	public void initialisePlateau() {
		preparePionsBlancs();
		preparePionsNoirs();
	}

	private void preparePionsBlancs() {
		for (int i = 0; i <= 7; i += 7) {
			Piece tourBlanche = new Tour(Couleur.WHITE,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = tourBlanche;
		}

		for (int i = 1; i <= 6; i += 5) {
			Piece cavalierBlanc = new Cavalier(Couleur.WHITE,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = cavalierBlanc;
		}

		for (int i = 2; i <= 5; i += 3) {
			Piece fouBlanc = new Fou(Couleur.WHITE,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = fouBlanc;
		}

		Piece reineBlanche = new Reine(Couleur.WHITE,
				PositionConverter.convertIndexEnCase(3));
		echiquier[3] = reineBlanche;

		Piece roiBlanc = new Roi(Couleur.WHITE,
				PositionConverter.convertIndexEnCase(4));
		echiquier[4] = roiBlanc;

		for (int i = 8; i <= 15; i++) {
			Piece pionBlanc = new Pion(Couleur.WHITE,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = pionBlanc;
		}
	}

	private void preparePionsNoirs() {
		for (int i = 56; i <= 63; i += 7) {
			Piece tour = new Tour(Couleur.BLACK,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = tour;
		}

		for (int i = 57; i <= 62; i += 5) {
			Piece cavalier = new Cavalier(Couleur.BLACK,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = cavalier;
		}

		for (int i = 58; i <= 61; i += 3) {
			Piece fou = new Fou(Couleur.BLACK,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = fou;
		}

		Piece reine = new Reine(Couleur.BLACK,
				PositionConverter.convertIndexEnCase(59));
		echiquier[59] = reine;

		Piece roi = new Roi(Couleur.BLACK,
				PositionConverter.convertIndexEnCase(60));
		echiquier[60] = roi;

		for (int i = 48; i <= 55; i++) {
			Piece pion = new Pion(Couleur.BLACK,
					PositionConverter.convertIndexEnCase(i));
			echiquier[i] = pion;
		}
	}

	/**
	 * Avance un pion sur l'�chiquier
	 * 
	 * @param coup
	 * @throws GameException
	 */
	public void jouerCoup(Coup coup) throws GameException {
		int indexSource = PositionConverter.convertCaseEnIndex(coup
				.getCaseSource());
		int indexDest = PositionConverter.convertCaseEnIndex(coup
				.getCaseDestination());

		Piece piece = this.getEchiquier()[indexSource];

		if (piece == null)
			throw new GameException("Etat de l'�chiquier incoh�rent");

		coup.setPieceSource(this.getEchiquier()[indexSource]);
		coup.setPieceDestination(this.getEchiquier()[indexDest]);

		this.getEchiquier()[indexSource] = null;
		this.getEchiquier()[indexDest] = piece;
		this.setDernierCoup(coup);

		piece.setPosition(coup.getCaseDestination());
	}

	/**
	 * Annule le coup pr�c�dent
	 */
	public void reculerUnCoup() {
		if (dernierCoup != null) {
			this.getEchiquier()[PositionConverter.convertCaseEnIndex(dernierCoup
					.getCaseSource())] = dernierCoup.getPieceSource();
			this.getEchiquier()[PositionConverter.convertCaseEnIndex(dernierCoup
					.getCaseDestination())] = dernierCoup.getPieceDestination();
			
			this.dernierCoup = null;
		}
	}

	/**
	 * V�rifie si la case pass�e en param�tre est vide ou non
	 * 
	 * @param nCase
	 * @return true si la case est vide false sinon
	 */
	public boolean isCaseVide(Case nCase) {
		int index = PositionConverter.convertCaseEnIndex(nCase);
		return this.getEchiquier()[index] == null;
	}

	/**
	 * Renvoie la couleur de la pi�ce se trouvant sur la case pass� en param�tre
	 * 
	 * @param nCase
	 * @return La couleur de la pi�ce, null si la case est vide
	 */
	public Couleur getCouleurPiece(Case nCase) {
		int index = PositionConverter.convertCaseEnIndex(nCase);

		if (this.getEchiquier()[index] == null)
			return null;
		else
			return this.getEchiquier()[index].getColor();
	}

	public Piece getPiece(Case nCase) {
		int index = PositionConverter.convertCaseEnIndex(nCase);

		return this.getEchiquier()[index];
	}

	/**
	 * Contr�le que toutes les cases d'une m�me colonne entre une case de d�part
	 * et une case d'arriv�e sont vides
	 * 
	 * @param caseSrc
	 *            La case de d�part
	 * @param caseDst
	 *            La case d'arriv�e
	 * @param sens
	 *            Le sens de d�placement
	 * @return True si toutes les cases sont vides. False sinon
	 */
	public boolean caseIntermVides(Case caseSrc, Case caseDst, Sens sens) {
		Case caseInter = new Case(caseSrc.getColonne(), caseSrc.getLigne());

		while (!caseInter.equals(caseDst)) {
			caseInter.setLigne(caseInter.getLigne() + sens.getModifLigne());
			caseInter.setColonne(caseInter.getColonne()
					+ sens.getModifColonne());

			if (!caseInter.equals(caseDst)) {
				if (!isCaseVide(caseInter))
					return false;
			}
		}

		return true;
	}

	/**
	 * V�rifie que les cases pass�es en param�tres sont valides pour l'�chiquier
	 * 
	 * @param indexSource
	 * @param indexDestination
	 * @return
	 */
	public boolean isCasesValides(Case caseSrc, Case caseDest) {
		int indexSource = PositionConverter.convertCaseEnIndex(caseSrc);
		int indexDestination = PositionConverter.convertCaseEnIndex(caseDest);

		if (indexSource < 0 || indexSource >= getEchiquier().length)
			return false;

		if (indexDestination < 0 || indexDestination >= getEchiquier().length)
			return false;

		return true;
	}

	/**
	 * V�rifie que la case caseDest contient une pi�ce de la m�me couleur que le
	 * param�tre joueurCouleur
	 * 
	 * @param indexDestination
	 * @return
	 */
	public boolean isDestOccupee(Case caseDest, Couleur joueurCouleur) {
		int indexDestination = PositionConverter.convertCaseEnIndex(caseDest);

		if (getEchiquier()[indexDestination] != null
				&& getEchiquier()[indexDestination].getColor().equals(
						joueurCouleur))
			return false;

		return true;
	}

	/**
	 * V�rifie que la pi�ce s�lectionn� par le joueur lui appartient.
	 * 
	 * @param indexSource
	 * @return
	 */
	public boolean isPieceDuJoueur(Case caseSrc, Couleur joueurCouleur) {
		int indexSource = PositionConverter.convertCaseEnIndex(caseSrc);

		if (getEchiquier()[indexSource] == null)
			return false;

		if (getEchiquier()[indexSource].getColor().equals(joueurCouleur))
			return true;

		return false;
	}

	/**
	 * Contr�le si l'action en cours est un mouvement d'attaque vers une case
	 * contr�l�e par l'adversaire
	 * 
	 * @param coup
	 * @return True si la case cible est contr�l�e par l'adversaire.
	 */
	public boolean isAttaque(Case caseDest, Couleur joueurCouleur) {
		int indexDestination = PositionConverter.convertCaseEnIndex(caseDest);

		Piece piece = getEchiquier()[indexDestination];

		if (piece != null && !piece.getColor().equals(joueurCouleur))
			return true;

		return false;
	}

	/**
	 * Renvoie vraie si la case cibl�e peut-�tre attaqu�e au tour suivant par
	 * l'adversaire
	 * 
	 * @param nCase
	 * @param couleurAttaquant
	 * @return
	 */
	public boolean caseMenacee(Case nCase, Couleur couleurAttaquant) {
		for (int index = 0; index < echiquier.length; index++) {
			Piece piece = echiquier[index];

			if (piece != null && piece.getColor() == couleurAttaquant) {
				if (piece.deplacementPossible(
						new Coup(PositionConverter.convertIndexEnCase(index),
								nCase), this, true))
					return true;
			}
		}

		return false;
	}

	/**
	 * Retourne le roi de la couleur sp�cifi�e
	 * 
	 * @param couleur
	 * @return
	 */
	public Piece getRoi(Couleur couleur) {
		for (int index = 0; index < echiquier.length; index++) {
			Piece piece = echiquier[index];

			if (piece != null && piece instanceof Roi
					&& piece.getColor() == couleur)
				return piece;
		}

		return null;
	}

	@Override
	public String toString() {
		StringBuilder echiquierRep = new StringBuilder("");
		int compteur = 0;

		for (int index = echiquier.length - 1; index >= 0; index--) {

			echiquierRep.append("|");

			if (echiquier[index] != null) {
				echiquierRep.append(echiquier[index].getClass().getSimpleName()
						.substring(0, 2));
				echiquierRep.append(echiquier[index].getColor().toString()
						.substring(0, 1));
			} else
				echiquierRep.append("---");

			echiquierRep.append("|");

			compteur++;

			if (compteur == 8) {
				echiquierRep.append("\n");
				compteur = 0;
			}
		}

		return echiquierRep.toString();
	}

	public Piece[] getEchiquier() {
		return echiquier;
	}

	public void setEchiquier(Piece[] echiquier) {
		this.echiquier = echiquier;
	}

	public Coup getDernierCoup() {
		return dernierCoup;
	}

	public void setDernierCoup(Coup dernierCoup) {
		this.dernierCoup = dernierCoup;
	}
}
