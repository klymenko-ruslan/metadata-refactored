package com.turbointernational.metadata.entity.chlogsrc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

@Entity
@Table(name = "changelog_source_link_description_attachment")
@NamedQueries({ @NamedQuery(name = "findOrphanAttachments", query = "SELECT o FROM ChangelogSourceLinkDescriptionAttachment o "
    + "WHERE o.changelogSourceLink IS NULL AND CURRENT_TIMESTAMP - o.created >= :period") })
@JsonInclude(ALWAYS)
public class ChangelogSourceLinkDescriptionAttachment implements Serializable {

  private static final long serialVersionUID = 1402442179625795907L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @JsonView(View.Summary.class)
  private Long id;

  @Temporal(TIMESTAMP)
  @Column(name = "created", nullable = false)
  @JsonView(View.Summary.class)
  private Date created;

  @Column(name = "name", length = 255, nullable = true)
  @JsonView(View.Summary.class)
  private String name;

  @Column(name = "original_name", length = 255, nullable = true)
  @JsonView(View.Summary.class)
  private String originalName;

  @Column(name = "size", nullable = true)
  @JsonView(View.Summary.class)
  private Long size;

  @Column(name = "mime", length = 255, nullable = false)
  @JsonView(View.Summary.class)
  private String mime;

  @Column(name = "filename", length = 1024, nullable = true)
  @JsonView(View.Summary.class)
  private String filename;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "changelog_source_link_id", nullable = true)
  @JsonView({ View.Summary.class })
  @JsonBackReference
  private ChangelogSourceLink changelogSourceLink;

  public ChangelogSourceLinkDescriptionAttachment() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public ChangelogSourceLink getChangelogSourceLink() {
    return changelogSourceLink;
  }

  public void setChangelogSourceLink(ChangelogSourceLink changelogSourceLink) {
    this.changelogSourceLink = changelogSourceLink;
  }

  @Override
  public String toString() {
    return "ChangelogSourceLinkDescriptionAttachment [id=" + id + ", created=" + created + ", name=" + name
        + ", originalName=" + originalName + ", size=" + size + ", mime=" + mime + ", filename=" + filename
        + ", changelogSourceLink=" + changelogSourceLink + "]";
  }

}
