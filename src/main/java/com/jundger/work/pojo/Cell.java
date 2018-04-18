package com.jundger.work.pojo;

import java.util.Date;

public class Cell {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.terminal_id
     *
     * @mbggenerated
     */
    private Integer terminalId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.use_count
     *
     * @mbggenerated
     */
    private Integer useCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.recent_use
     *
     * @mbggenerated
     */
    private Date recentUse;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.book_flag
     *
     * @mbggenerated
     */
    private Integer bookFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cell.detail
     *
     * @mbggenerated
     */
    private String detail;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cell
     *
     * @mbggenerated
     */
    public Cell(Integer id, Integer terminalId, String status, String type, Integer useCount, Date recentUse, Integer bookFlag, String detail) {
        this.id = id;
        this.terminalId = terminalId;
        this.status = status;
        this.type = type;
        this.useCount = useCount;
        this.recentUse = recentUse;
        this.bookFlag = bookFlag;
        this.detail = detail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cell
     *
     * @mbggenerated
     */
    public Cell() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.id
     *
     * @return the value of cell.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.id
     *
     * @param id the value for cell.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.terminal_id
     *
     * @return the value of cell.terminal_id
     *
     * @mbggenerated
     */
    public Integer getTerminalId() {
        return terminalId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.terminal_id
     *
     * @param terminalId the value for cell.terminal_id
     *
     * @mbggenerated
     */
    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.status
     *
     * @return the value of cell.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.status
     *
     * @param status the value for cell.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.type
     *
     * @return the value of cell.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.type
     *
     * @param type the value for cell.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.use_count
     *
     * @return the value of cell.use_count
     *
     * @mbggenerated
     */
    public Integer getUseCount() {
        return useCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.use_count
     *
     * @param useCount the value for cell.use_count
     *
     * @mbggenerated
     */
    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.recent_use
     *
     * @return the value of cell.recent_use
     *
     * @mbggenerated
     */
    public Date getRecentUse() {
        return recentUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.recent_use
     *
     * @param recentUse the value for cell.recent_use
     *
     * @mbggenerated
     */
    public void setRecentUse(Date recentUse) {
        this.recentUse = recentUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.book_flag
     *
     * @return the value of cell.book_flag
     *
     * @mbggenerated
     */
    public Integer getBookFlag() {
        return bookFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.book_flag
     *
     * @param bookFlag the value for cell.book_flag
     *
     * @mbggenerated
     */
    public void setBookFlag(Integer bookFlag) {
        this.bookFlag = bookFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cell.detail
     *
     * @return the value of cell.detail
     *
     * @mbggenerated
     */
    public String getDetail() {
        return detail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cell.detail
     *
     * @param detail the value for cell.detail
     *
     * @mbggenerated
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}