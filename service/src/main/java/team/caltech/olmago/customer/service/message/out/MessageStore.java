package team.caltech.olmago.customer.service.message.out;

import team.caltech.olmago.common.message.MessageEnvelope;

import java.util.List;

public interface MessageStore {
  void saveMessage(MessageEnvelope msg);
  void saveMessage(List<MessageEnvelope> msgs);
}
