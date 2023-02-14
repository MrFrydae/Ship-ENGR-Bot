package edu.ship.engr.discordbot.containers;

import edu.ship.engr.discordbot.utils.GuildUtil;
import lombok.Data;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import javax.annotation.CheckReturnValue;

/**
 * Only used to link all types of {@link MappedUser} together.
 */
@Data
public abstract class MappedUser {
    private final String name;
    protected final String email;
    protected final String discordId;
    @Getter(lazy = true) private final Member member = GuildUtil.getMember(discordId);
    protected final String major;

    /**
     * Changes the {@link Member member}'s nickname to their actual name.
     */
    @CheckReturnValue
    public AuditableRestAction<Void> setNickname() {
        return GuildUtil.setNickname(getMember(), getName());
    }
}
