package org.openchat.domain.entity;

public class Following {
    public final String followerId;
    public final String followeeId;

    public Following(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Following following = (Following) o;

        if (!followerId.equals(following.followerId)) return false;
        return followeeId.equals(following.followeeId);

    }

    @Override
    public int hashCode() {
        int result = followerId.hashCode();
        result = 31 * result + followeeId.hashCode();
        return result;
    }
}
