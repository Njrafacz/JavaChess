package com.javachess.jeu;

import com.javachess.exceptions.GameException;
import com.javachess.helpers.Coup;
import com.javachess.helpers.PositionConverter;
import com.javachess.joueurs.Joueur;
import com.javachess.modele.pieces.Piece;
import com.javachess.modele.plateau.Echiquier;

/**
 * Mod�lise une partie d'echec. Chaque partie correspond � un plateau et � deux
 * joueurs. Chaque joueur effectue un coup � tour de r�le jusqu'� ce que l'un
 * d'entre eux abandonne ou jusqu'� l'echec et mat.
 * 
 * @author Ouzned
 * 
 */
public class Partie {
	private Echiquier plateau;
	private Joueur joueur1;
	private Joueur joueur2;
	private Joueur joueurCourant;

	/**
	 * Initialisation de la partie
	 * 
	 * @param joueur1
	 * @param joueur2
	 */
	public Partie(Joueur joueur1, Joueur joueur2) {
		this.joueur1 = joueur1;
		this.joueur2 = joueur2;

		this.plateau = new Echiquier();

		joueurSuivant();
	}

	/**
	 * S�lectionne le joueur suivant
	 */
	private void joueurSuivant() {
		if (joueurCourant == null) {
			joueurCourant = joueur1;
			return;
		}

		if (joueurCourant == joueur1)
			joueurCourant = joueur2;
		else
			joueurCourant = joueur1;
	}

	/**
	 * D�marre la partie
	 * 
	 * @throws GameException
	 */
	public void start() throws GameException {
		while (!isFinished()) {
			System.out.println(plateau);
			Coup coup = joueurCourant.jouer();
			if (isCoupValide(coup) && plateau.jouerCoup(coup, isAttaque(coup))) {
				// etatPartie.verifierEtat();
				joueurSuivant();
			}
		}
	}

	// TODO: Gestion de l'echec, de l'echec et mat

	/**
	 * V�rifie que l'action en cours est valide : pas de d�placement sur une
	 * case contr�l�e par le m�me joueur, d�placement sur une case existante,
	 * d�placement d'une pi�ce de la m�me couleur que le joueur courant.
	 * 
	 * @param coup
	 * @return
	 */
	public boolean isCoupValide(Coup coup) {
		int indexSource = PositionConverter.convertCaseEnIndex(coup
				.getCaseSource());
		int indexDestination = PositionConverter.convertCaseEnIndex(coup
				.getCaseDestination());

		if (indexSource < 0 || indexSource >= plateau.getEchiquier().length)
			return false;

		if (indexDestination < 0
				|| indexDestination >= plateau.getEchiquier().length)
			return false;

		if (!plateau.getEchiquier()[indexSource].getColor().equals(
				joueurCourant.getCouleur()))
			return false;

		if (plateau.getEchiquier()[indexDestination] != null
				&& plateau.getEchiquier()[indexDestination].getColor().equals(
						joueurCourant.getCouleur()))
			return false;

		if (coup.getCaseDestination().equals(coup.getCaseSource()))
			return false;

		return true;
	}

	/**
	 * Contr�le si l'action en cours est un mouvement d'attaque vers une case
	 * contr�l�e par l'adversaire
	 * 
	 * @param coup
	 * @return True si la case cible est contr�l�e par l'adversaire.
	 */
	public boolean isAttaque(Coup coup) {
		int indexDestination = PositionConverter.convertCaseEnIndex(coup
				.getCaseDestination());

		Piece piece = plateau.getEchiquier()[indexDestination];

		if (piece != null
				&& !piece.getColor().equals(joueurCourant.getCouleur()))
			return true;

		return false;
	}

	public boolean isFinished() {
		return false;
	}
}
