@Entity //esta clase se mapea a una tabla en la base de datos
public class MedicalRecord {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Genera el valor de la clave primaria utilizando un id
    private Long id; //ID único del registro médico

    @NotNull
    @ManyToOne //Relación muchos a uno, muchos registros médicos pueden pertenecer a un paciente
    @JoinColumn(name = "patient_id") //Nombre de la columna en la tabla de la base de datos
    private Patient patient; // Atributo que representa al paciente asociado al registro médico

    @NotNull
    private LocalDate recordDate; //Fecha del registro

    @NotNull
    private String description; //Descripción del registro médico

//obtener id
    public Long getId() {
        return id;
    }
//Set id
    public void setId(Long id) {
        this.id = id;
    }
//obtener paciente
    public Patient getPatient() {
        return patient;
    }
//Set paciente
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
//demás getters y setters
    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
