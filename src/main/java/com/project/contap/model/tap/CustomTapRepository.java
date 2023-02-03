package com.project.contap.model.tap;

import com.project.contap.model.user.User;

import java.util.List;

public interface CustomTapRepository {
    Tap checkReceiveTap(User receiver, User sender);
    List<Tap> getMyTaps(User user);
    Boolean checkSendTap(User receiver, User sender);
}
