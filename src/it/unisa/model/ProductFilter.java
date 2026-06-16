package it.unisa.model;

/**
 * Value object dei criteri di filtraggio del catalogo prodotti (CR_04).
 * POJO semplice (getter/setter), DI-friendly e predisposto ad estensioni (es. filtro per tipo).
 * I valori null su priceMin/priceMax significano "nessun bound" lato service.
 */
public class ProductFilter {

    private Float priceMin;
    private Float priceMax;
    private boolean inStockOnly;
    private String sortColumn;
    private String sortDir;

    public ProductFilter() {}

    public Float getPriceMin() { return priceMin; }
    public void setPriceMin(Float priceMin) { this.priceMin = priceMin; }

    public Float getPriceMax() { return priceMax; }
    public void setPriceMax(Float priceMax) { this.priceMax = priceMax; }

    public boolean isInStockOnly() { return inStockOnly; }
    public void setInStockOnly(boolean inStockOnly) { this.inStockOnly = inStockOnly; }

    public String getSortColumn() { return sortColumn; }
    public void setSortColumn(String sortColumn) { this.sortColumn = sortColumn; }

    public String getSortDir() { return sortDir; }
    public void setSortDir(String sortDir) { this.sortDir = sortDir; }
}
