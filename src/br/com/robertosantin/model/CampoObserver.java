package br.com.robertosantin.model;

@FunctionalInterface
public interface CampoObserver {
	
	public void eventoOcorreu(Campo campo, CampoEvento evento);
	
}
