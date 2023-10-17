package mutsa.sns.domain.entity;

public enum FriendStatus {
    REQUEST("요청"), ACCEPTED("수락"), REJECTED("거절");
    private String statusName;

    FriendStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatus() {
        return statusName;
    }
}
