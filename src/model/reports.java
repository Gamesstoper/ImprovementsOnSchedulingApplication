package model;

public class reports {
    private String months;
    private int consultation;
    private int workout;
    private int personal;
    private int newCustomerMeeting;
    private int nutritionMeeting;
    private int companyMeeting;
    private String createdBy;
    private String createDate;
    private int apptID;
    private String contact;

    /**reports.
     * @param month
     * @param workout
     * @param newCustomerMeeting
     * @param personal
     * @param consultation
     * This creates our main constructor for formatting and setting up our reports objects */
    public reports(String month, int consultation, int workout, int personal, int newCustomerMeeting, int nutritionMeeting
    , int companyMeeting){
        setMonths(month);
        setConsultation(consultation);
        setWorkout(workout);
        setPersonal(personal);
        setNewCustomerMeeting(newCustomerMeeting);
        setNutritionMeeting(nutritionMeeting);
        setCompanyMeeting(companyMeeting);
    }

    /**reports.
     * @param contact
     * @param apptID
     * @param createDate
     * @param createdBy
     * This creates another constructor for formatting and setting up our reports objects */
    public reports(String createdBy, String createDate, int apptID,String contact){
        setCreatedBy(createdBy);
        setCreateDate(createDate);
        setApptID(apptID);
        setContact(contact);
    }

    /**getMonths.
     * @return
     * Standard getter*/
    public String getMonths() {
        return months;
    }

    /**setMonths.
     * @param months
     * Standard setter*/
    public void setMonths(String months) {
        this.months = months;
    }

    /**getPlanning.
     * @return
     * Standard getter*/
    public int getConsultation() {
        return consultation;
    }

    /**setPlanning.
     * @param consultation
     * Standard setter*/
    public void setConsultation(int consultation) {
        this.consultation = consultation;
    }

    /**getBrief.
     * @return
     * Standard getter*/
    public int getWorkout() {
        return workout;
    }

    /**setBrief.
     * @param workout
     * Standard setter*/
    public void setWorkout(int workout) {
        this.workout = workout;
    }

    /**getPersonal.
     * @return
     * Standard getter*/
    public int getPersonal() {
        return personal;
    }

    /**setPersonal.
     * @param personal
     * Standard setter*/
    public void setPersonal(int personal) {
        this.personal = personal;
    }

    /**getCloseAccount.
     * @return
     * Standard getter*/
    public int getNewCustomerMeeting() {
        return newCustomerMeeting;
    }

    /**setCloseAccount.
     * @param newCustomerMeeting
     * Standard setter*/
    public void setNewCustomerMeeting(int newCustomerMeeting) {
        this.newCustomerMeeting = newCustomerMeeting;
    }

    /**getCreatedBy.
     * @return
     * Standard getter*/
    public String getCreatedBy() {
        return createdBy;
    }

    /**setCreatedBy.
     * @param createdBy
     * Standard setter*/
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**getCreateDate.
     * @return
     * Standard getter*/
    public String getCreateDate() {
        return createDate;
    }

    /**setCreateDate.
     * @param createDate
     * Standard setter*/
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**getApptID.
     * @return
     * Standard getter*/
    public int getApptID() {
        return apptID;
    }

    /**setApptID.
     * @param apptID
     * Standard setter*/
    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    /**getContact.
     * @return
     * Standard getter*/
    public String getContact() {
        return contact;
    }

    /**setContact.
     * @param contact
     * Standard setter*/
    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getNutritionMeeting() {
        return nutritionMeeting;
    }

    public void setNutritionMeeting(int nutritionMeeting) {
        this.nutritionMeeting = nutritionMeeting;
    }

    public int getCompanyMeeting() {
        return companyMeeting;
    }

    public void setCompanyMeeting(int companyMeeting) {
        this.companyMeeting = companyMeeting;
    }
}
