package gov.epa.ccte.api.rapidtox.chemical.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

/**
 *
 * @author arashid
 * Create at 2021-07-13 13:06
 */
@Entity
@Table(name = "chemical_details", schema = "rapidtox")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChemicalDetails {

    /**
     *
     */
    @Id
    @Column
    private Integer Id;

    /**
     *
     */
    @Column(name = "dtxsid", length = 45)
    private String dtxsid;

    /**
     *
     */
    @Column(name = "dtxcid", length = 45)
    private String dtxcid;

    /**
     *
     */
    @Column(name = "casrn")
    private String casrn;

    /**
     *
     */
    @Column(name = "compound_id")
    private Integer compoundId;

    /**
     *
     */
    @Column(name = "generic_substance_id")
    private Integer genericSubstanceId;

    /**
     *
     */
    @Column(name = "preferred_name")
    private String preferredName;

    /**
     *
     */
    @Column(name = "active_assays")
    private Integer activeAssays;

    /**
     *
     */
    @Column(name = "cpdata_count")
    private Long cpdataCount;

    /**
     *
     */
    @Column(name = "mol_formula")
    private String molFormula;

    /**
     *
     */
    @Column(name = "monoisotopic_mass")
    private BigDecimal monoisotopicMass;

    /**
     *
     */
    @Column(name = "percent_assays")
    private BigDecimal percentAssays;

    /**
     *
     */
    @Column(name = "pubchem_count")
    private Integer pubchemCount;

    /**
     *
     */
    @Column(name = "pubmed_count")
    private BigDecimal pubmedCount;

    /**
     *
     */
    @Column(name = "sources_count")
    private Long sourcesCount;

    /**
     *
     */
    @Column(name = "qc_level")
    private Integer qcLevel;

    /**
     *
     */
    @Column(name = "qc_level_desc")
    private String qcLevelDesc;

    /**
     *
     */
    @Column(name = "stereo", length = 1)
    private String stereo;

    /**
     *
     */
    @Column(name = "isotope", precision = 5)
    private BigDecimal isotope;

    /**
     *
     */
    @Column(name = "multicomponent")
    private Integer multicomponent;

    /**
     *
     */
    @Column(name = "total_assays")
    private Integer totalAssays;

    /**
     *
     */
    @Column(name = "toxcast_select")
    private String toxcastSelect;

    /**
     *
     */
    @Column(name = "pubchem_cid")
    private Integer pubchemCid;

    /**
     *
     */
    @Column(name = "mol_file")
    private String molFile;

    /**
     *
     */
    @Column(name = "mrv_file")
    private String mrvFile;

    /**
     *
     */
    @Column(name = "related_substance_count")
    private Long relatedSubstanceCount;

    /**
     *
     */
    @Column(name = "related_structure_count")
    private Long relatedStructureCount;

    /**
     *
     */
    @Column(name = "mol_image")
    private byte[] molImage;

    /**
     *
     */
    @Column(name = "iupac_name", length = 5000)
    private String iupacName;

    /**
     *
     */
    @Column(name = "smiles")
    private String smiles;

    /**
     *
     */
    @Column(name = "inchi_string")
    private String inchiString;

    /**
     *
     */
    @Column(name = "average_mass")
    private BigDecimal averageMass;

    /**
     *
     */
    @Column(name = "inchikey")
    private String inchikey;

    /**
     *
     */
    @Column(name = "qc_notes", length = 4000)
    private String qcNotes;

    /**
     *
     */
    @Column(name = "ms_ready_smiles")
    private String msReadySmiles;

    /**
     *
     */
    @Column(name = "qsar_ready_smiles")
    private String qsarReadySmiles;

    /**
     *
     */
    @Column(name = "iris_link")
    private String irisLink;

    /**
     *
     */
    @Column(name = "pprtv_link")
    private String pprtvLink;

    /**
     *
     */
    @Column(name = "wikipedia_article")
    private String wikipediaArticle;

    /**
     *
     */
    @Column(name = "water_solubility_test")
    private BigDecimal waterSolubilityTest;

    /**
     *
     */
    @Column(name = "water_solubility_opera")
    private BigDecimal waterSolubilityOpera;

    /**
     *
     */
    @Column(name = "viscosity_cp_cp_test_pred")
    private BigDecimal viscosityCpCpTestPred;

    /**
     *
     */
    @Column(name = "vapor_pressure_mmhg_test_pred")
    private BigDecimal vaporPressureMmhgTestPred;

    /**
     *
     */
    @Column(name = "vapor_pressure_mmhg_opera_pred")
    private BigDecimal vaporPressureMmhgOperaPred;

    /**
     *
     */
    @Column(name = "thermal_conductivity")
    private BigDecimal thermalConductivity;

    /**
     *
     */
    @Column(name = "tetrahymena_pyriformis")
    private BigDecimal tetrahymenaPyriformis;

    /**
     *
     */
    @Column(name = "surface_tension")
    private BigDecimal surfaceTension;

    /**
     *
     */
    @Column(name = "soil_adsorption_coefficient")
    private BigDecimal soilAdsorptionCoefficient;

    /**
     *
     */
    @Column(name = "oral_rat_ld50_mol")
    private BigDecimal oralRatLd50Mol;

    /**
     *
     */
    @Column(name = "opera_km_days_opera_pred")
    private BigDecimal operaKmDaysOperaPred;

    /**
     *
     */
    @Column(name = "octanol_water_partition")
    private BigDecimal octanolWaterPartition;

    /**
     *
     */
    @Column(name = "octanol_air_partition_coeff")
    private BigDecimal octanolAirPartitionCoeff;

    /**
     *
     */
    @Column(name = "melting_point_degc_test_pred")
    private BigDecimal meltingPointDegcTestPred;

    /**
     *
     */
    @Column(name = "melting_point_degc_opera_pred")
    private BigDecimal meltingPointDegcOperaPred;

    /**
     *
     */
    @Column(name = "hr_fathead_minnow")
    private BigDecimal hrFatheadMinnow;

    /**
     *
     */
    @Column(name = "hr_diphnia_lc50")
    private BigDecimal hrDiphniaLc50;

    /**
     *
     */
    @Column(name = "henrys_law_atm")
    private BigDecimal henrysLawAtm;

    /**
     *
     */
    @Column(name = "flash_point_degc_test_pred")
    private BigDecimal flashPointDegcTestPred;

    /**
     *
     */
    @Column(name = "devtox_test_pred")
    private BigDecimal devtoxTestPred;

    /**
     *
     */
    @Column(name = "density")
    private BigDecimal density;

    /**
     *
     */
    @Column(name = "boiling_point_degc_test_pred")
    private BigDecimal boilingPointDegcTestPred;

    /**
     *
     */
    @Column(name = "boiling_point_degc_opera_pred")
    private BigDecimal boilingPointDegcOperaPred;

    /**
     *
     */
    @Column(name = "biodegradation_half_life_days")
    private BigDecimal biodegradationHalfLifeDays;

    /**
     *
     */
    @Column(name = "bioconcentration_factor_test_pred")
    private BigDecimal bioconcentrationFactorTestPred;

    /**
     *
     */
    @Column(name = "bioconcentration_factor_opera_pred")
    private BigDecimal bioconcentrationFactorOperaPred;

    /**
     *
     */
    @Column(name = "atmospheric_hydroxylation_rate")
    private BigDecimal atmosphericHydroxylationRate;

    /**
     *
     */
    @Column(name = "ames_mutagenicity_test_pred")
    private BigDecimal amesMutagenicityTestPred;

    /**
     *
     */
    @Column(name = "descriptor_string_tsv", length = 20000)
    private String descriptorStringTsv;

    /**
     *
     */
    @Column(name = "created_timestamp")
    private OffsetDateTime createdTimestamp;
}
