package in.amtron.zoooperator.model;

public class BookingDetails {

    /*
        "date": "2021-10-03",
        "ticket_no": "",
        "visitor_type": "I",
        "adult": 1,
        "student": 2,
        "children": 0,
        "still":0,
        "dslr":0,
        "video":0,
        "pos_txn": ""
     */

    private String date;
    private String ticketNo;
    private String visitorType;
    private int adult;
    private int student;
    private int children;
    private int still;
    private int dslr;
    private int video;
    private String postTxn;

    public BookingDetails(String visitorType, int adult, int student, int children, int still, int dslr, int video) {
        this.visitorType = visitorType;
        this.adult = adult;
        this.student = student;
        this.children = children;
        this.still = still;
        this.dslr = dslr;
        this.video = video;

        // modify data
        date = "";
        ticketNo = "";
        postTxn = "";
    }

    public String getDate() {
        return date;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getVisitorType() {
        return visitorType;
    }

    public int getAdult() {
        return adult;
    }

    public int getStudent() {
        return student;
    }

    public int getChildren() {
        return children;
    }

    public int getStill() {
        return still;
    }

    public int getDslr() {
        return dslr;
    }

    public int getVideo() {
        return video;
    }

    public String getPostTxn() {
        return postTxn;
    }
}
