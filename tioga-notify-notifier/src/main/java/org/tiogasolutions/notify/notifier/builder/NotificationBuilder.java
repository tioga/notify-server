package org.tiogasolutions.notify.notifier.builder;

import org.tiogasolutions.notify.notifier.request.*;
import org.tiogasolutions.notify.notifier.sender.NotificationSender;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Future;

/**
 * User: Harlan
 * Date: 1/24/2015
 * Time: 10:44 PM
 */
public class NotificationBuilder {
  private final NotificationSender sender;
  private final NotificationBuilderCallbacks callbacks;
  private String topic;
  private String trackingId;
  private String summary;
  private ZonedDateTime createdAt;
  private NotificationExceptionInfo exceptionInfo;
  private List<NotificationTrait> traits = new ArrayList<>();
  private List<NotificationLink> links = new ArrayList<>();
  private List<NotificationAttachment> attachments = new ArrayList<>();

  public NotificationBuilder(NotificationSender sender, NotificationBuilderCallbacks callbacks) {
    this.sender = sender;
    this.callbacks = callbacks.copy();
    callbacks.callBegin(this);
  }

  public NotificationBuilder onBegin(NotificationBuilderCallback callback) {
    callbacks.onBegin(callback);
    return this;
  }

  public NotificationBuilder onBeforeSend(NotificationBuilderCallback callback) {
    callbacks.onBeforeSend(callback);
    return this;
  }

  public Future<NotificationResponse> send() {
    callbacks.callBeforeSend(this);
    NotificationRequest request = new NotificationRequest(
      topic,
      summary,
      trackingId,
      createdAt,
      NotificationTrait.toTraitMap(traits),
      links,
      exceptionInfo,
      attachments);

    return sender.send(request);
  }

  public NotificationBuilder topic(String topic) {
    this.topic = topic;
    return this;
  }

  public NotificationBuilder trackingId(String trackingId) {
    this.trackingId = trackingId;
    return this;
  }

  public NotificationBuilder summary(String summary) {
    this.summary = summary;
    return this;
  }

  public NotificationBuilder summary(String format, Object... args) {
    this.summary = String.format(format, args);
    return this;
  }

  public NotificationBuilder createdAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public NotificationBuilder trait(String name, Object value) {
    String str = (value != null) ? value.toString() : null;
    traits.add(new NotificationTrait(name, str));
    return this;
  }

  public NotificationBuilder traits(String... traits) {
    this.traits.addAll(NotificationTrait.toTraits(Arrays.asList(traits)));
    return this;
  }

  public NotificationBuilder traits(Map<String, String> traitMap) {
    this.traits.addAll(NotificationTrait.toTraits(traitMap));
    return this;
  }

  public NotificationBuilder link(String name, String href) {
    this.links.add(new NotificationLink(name, href));
    return this;
  }

  public NotificationBuilder links(Collection<NotificationLink> linksArg) {
    this.links.addAll(linksArg);
    return this;
  }

  public NotificationBuilder links(NotificationLink link) {
    this.links.add(link);
    return this;
  }

  public NotificationBuilder exception(Throwable exception) {
    this.exceptionInfo = new NotificationExceptionInfo(exception);
    return this;
  }

  public NotificationBuilder exception(NotificationExceptionInfo exceptionInfo) {
    this.exceptionInfo = exceptionInfo;
    return this;
  }

  public NotificationBuilder attach(String name, String contentType, byte[] content) {
    this.attachments.add(new NotificationAttachment(name, contentType, content));
    return this;
  }

  public NotificationBuilder attach(String name, String contentType, InputStream inputStream) {
    this.attachments.add(new NotificationAttachment(name, contentType, inputStream));
    return this;
  }

  public NotificationBuilder attach(String name, String contentType, String content) {
    this.attachments.add(new NotificationAttachment(name, contentType, content));
    return this;
  }

  public String getTopic() {
    return topic;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public String getSummary() {
    return summary;
  }

  public NotificationExceptionInfo getExceptionInfo() {
    return exceptionInfo;
  }

  public List<NotificationTrait> getTraits() {
    return traits;
  }

  public List<NotificationAttachment> getAttachments() {
    return attachments;
  }
}