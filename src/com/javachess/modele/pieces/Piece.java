package com.javachess.modele.pieces;

import com.javachess.helpers.Couleur;
import com.javachess.helpers.Coup;
import com.javachess.modele.plateau.Echiquier;

//TODO: une piece A un mode de d�placement ? Ca pourrait �tre plus pratique...

/**
 * Cette classe repr�sente le template d'une pi�ce d'�chec. Chaque pi�ce est
 * repr�sent�e par une couleur et une position.
 * 
 * @author Ouzned
 */
public abstract class Piece {
	private Couleur color;
	
	public Piece(Couleur couleur) {
		this.color = couleur;
	}
	
	/**
	 * V�rifie si la pi�ce peut se d�placer � newPos Cette m�thode ne v�rifie
	 * pas les mouvements d'attaque d'une pi�ce (mouvement diagonal pour le pion
	 * par exemple)
	 * 
	 * @param newPos
	 * @return Vrai si la pi�ce peut se d�placer en newPos
	 */
	public abstract boolean attaquePossible(Coup coup, Echiquier echiquier);

	/**
	 * V�rifie si la pi�ce peut attaquer sur newPos.
	 * 
	 * @param newPos
	 * @return Vrai si la pi�ce peut attaquer (et prendre une pi�ce adverse)
	 */
	public abstract boolean mouvementPossible(Coup coup, Echiquier echiquier);

	/**
	 * La pi�ce est-elle capable de sauter au dessus d'un autre ?
	 * 
	 * @return
	 */
	public boolean canJumpOver() {
		return false;
	}

	public Couleur getColor() {
		return color;
	}

	public void setColor(Couleur color) {
		this.color = color;
	}
}
