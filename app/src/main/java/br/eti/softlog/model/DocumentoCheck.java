package br.eti.softlog.model;

public class DocumentoCheck {
    private boolean isSelected;
    private Documento documento;

    public Documento getDocumento() {
        return documento;
    }

    public void setAnimal(Documento documento) {
        this.documento = documento;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
