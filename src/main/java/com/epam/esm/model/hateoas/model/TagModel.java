package com.epam.esm.model.hateoas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

public class TagModel extends RepresentationModel<TagModel> {
    private Long id;
    private String name;
    @JsonIgnore
    private Set<GiftCertificateModel> giftCertificates = new HashSet<>();

    public TagModel() {
    }

    public TagModel(Long id, String name, Set<GiftCertificateModel> giftCertificates) {
        this.id = id;
        this.name = name;
        this.giftCertificates = giftCertificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GiftCertificateModel> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificateModel> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TagModel tagModel = (TagModel) o;

        if (id != null ? !id.equals(tagModel.id) : tagModel.id != null) return false;
        if (name != null ? !name.equals(tagModel.name) : tagModel.name != null) return false;
        return giftCertificates != null ? giftCertificates.equals(tagModel.giftCertificates) : tagModel.giftCertificates == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (giftCertificates != null ? giftCertificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagModel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", giftCertificates=").append(giftCertificates);
        sb.append('}');
        return sb.toString();
    }
}
