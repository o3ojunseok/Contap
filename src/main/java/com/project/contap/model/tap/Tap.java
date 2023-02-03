package com.project.contap.model.tap;

import com.project.contap.common.util.TimeStamped;
import com.project.contap.model.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Tap extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @OneToOne(fetch = FetchType.LAZY)
    private User receiveUser;

    @Column
    private int status;//0 : 대기 - 1 : 거절 - 2 : 수락

    @Column
    private String msg;

    @Column
    private int newFriend; // 1: new
}
