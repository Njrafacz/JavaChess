package com.javachess.jeu;

import com.javachess.exceptions.GameException;
import com.javachess.helpers.Couleur;
import com.javachess.helpers.Coup;
import com.javachess.joueurs.Joueur;
import com.javachess.modele.pieces.Piece;
import com.javachess.modele.pieces.Roi;
import com.javachess.modele.plateau.Case;
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
	private boolean finished = false;

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

		setJoueurCourant(joueurSuivant());
	}

	/**
	 * S�lectionne le joueur suivant
	 */
	private Joueur joueurSuivant() {
		Joueur joueurSuivant = null;

		if (joueurCourant == null) {
			joueurSuivant = joueur1;
		} else {
			if (joueurCourant == joueur1)
				joueurSuivant = joueur2;
			else
				joueurSuivant = joueur1;
		}

		return joueurSuivant;
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
			Piece piece = plateau.getPiece(coup.getCaseSource());

			if (isCoupValide(coup, joueurCourant.getCouleur())
					&& piece.deplacementPossible(coup, plateau, plateau
							.isAttaque(coup.getCaseDestination(),
									joueurCourant.getCouleur()))) {

				plateau.jouerCoup(coup);

				controlerEtat();
				setJoueurCourant(joueurSuivant());
			}
		}

		System.out.println("Fin de partie!!!!!");
	}

	/**
	 * V�rifie que l'action en cours est valide : pas de d�placement sur une
	 * case contr�l�e par le m�me joueur, d�placement sur une case existante,
	 * d�placement d'une pi�ce de la m�me couleur que le joueur courant.
	 * 
	 * @param coup
	 * @return
	 */
	public boolean isCoupValide(Coup coup, Couleur joueur) throws GameException {
		if (!plateau.isCasesValides(coup.getCaseSource(),
				coup.getCaseDestination()))
			return false;

		if (!plateau.isPieceDuJoueur(coup.getCaseSource(), joueur))
			return false;

		if (!plateau.isDestOccupee(coup.getCaseDestination(), joueur))
			return false;

		if (coup.getCaseDestination().equals(coup.getCaseSource()))
			return false;

		if (!verifierEchec(coup))
			return false;

		return true;
	}

	/**
	 * Contr�le la validit� du coup en fonction d'un �ventuel echec
	 * 
	 * @param coup
	 * @return
	 * @throws GameException
	 */
	public boolean verifierEchec(Coup coup) throws GameException {
		Roi roi = (Roi) plateau.getRoi(joueurCourant.getCouleur());
		boolean result = true;

		plateau.jouerCoup(coup);
		if (plateau
				.caseMenacee(roi.getPosition(), joueurSuivant().getCouleur())) {
			System.out.println("Coup impossible : Echec");
			result = false;
		}

		plateau.reculerUnCoup();

		return result;
	}

	/**
	 * V�rifie l'�tat du jeu apr�s le coup du joueur courant (notamment
	 * l'�v�nement Echec et mat)
	 * 
	 */
	public void controlerEtat() throws GameException {
		Roi roi = (Roi) plateau.getRoi(joueurSuivant().getCouleur());

		if (plateau
				.caseMenacee(roi.getPosition(), joueurSuivant().getCouleur())) {
			roi.setEchec(true);
			System.out.println("Echec");
		} else
			roi.setEchec(false);

		if (roi.isEchec() && isEchecEtMat())
			setFinished(true);
	}

	public boolean isEchecEtMat() throws GameException {
		Piece[] pieces = plateau.getPions(joueurSuivant().getCouleur());
		Case[] cases = plateau.getCases();

		for (int iPiece = 0; iPiece < pieces.length; iPiece++) {
			for (int iCase = 0; iCase < cases.length; iCase++) {
				Coup coup = new Coup(pieces[iPiece].getPosition(), cases[iCase]);
				
				if (isCoupValide(coup, joueurSuivant().getCouleur())
						&& pieces[iPiece].deplacementPossible(coup, plateau,
								plateau.isAttaque(coup.getCaseDestination(),
										joueurSuivant().getCouleur())))
					return false;
			}
		}

		return true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setJoueurCourant(Joueur joueur) {
		this.joueurCourant = joueur;
	}
}
