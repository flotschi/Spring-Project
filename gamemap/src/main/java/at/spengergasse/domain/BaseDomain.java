package at.spengergasse.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;


@MappedSuperclass
@NoArgsConstructor
@Getter @Setter @ToString (callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain extends AbstractPersistable<Long> {

    @Version
    private Integer version;

    @CreatedDate
    private LocalDateTime createdAt;
}
