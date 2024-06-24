package fa.training.interviewmanagement.model.candidate;

public class CandidateEnum {
    public enum CurrentPosition {
        Backend, BA, Tester, HR, PM, NotAvailable
    }

    public enum CandidateStatus {
        Open("Open"), WaitingForInterview("Waiting For Interview"), Canceled("Canceled"), Passed("Passed"), Failed("Failed"), WaitingForApproval("Waiting For Approval"),
        Approved("Approved"), Rejected("Approved"), WaitingForResponse("Waiting For Response"), AcceptedOffer("Accepted Offer"), DeclinedOffer("Declined Offer"), CancelledOffer("Cancelled Offer"), Banned("Banned");
        private final String displayName;

        CandidateStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum CandidateGender {
        Male, Female
    }
    public enum CandidateHighestLevel{
        HighSchool("High School"), BachelorDegree("Bachelor's Degree"), MasterDegree("Master Degree"), PhD("PhD");
        private final String displayName;

        CandidateHighestLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum CandidateSkill{
        Java("Java"), Nodejs("Nodejs"), Net(".Net"), Cpp("C++"), BA("Business Analyst"), Communication("Communication");

        private final String displayName;

        CandidateSkill(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
