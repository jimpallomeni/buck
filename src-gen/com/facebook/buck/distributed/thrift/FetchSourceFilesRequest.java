/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.facebook.buck.distributed.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2017-05-03")
public class FetchSourceFilesRequest implements org.apache.thrift.TBase<FetchSourceFilesRequest, FetchSourceFilesRequest._Fields>, java.io.Serializable, Cloneable, Comparable<FetchSourceFilesRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("FetchSourceFilesRequest");

  private static final org.apache.thrift.protocol.TField CONTENT_HASHES_FIELD_DESC = new org.apache.thrift.protocol.TField("contentHashes", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new FetchSourceFilesRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new FetchSourceFilesRequestTupleSchemeFactory());
  }

  public List<String> contentHashes; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CONTENT_HASHES((short)1, "contentHashes");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // CONTENT_HASHES
          return CONTENT_HASHES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final _Fields optionals[] = {_Fields.CONTENT_HASHES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CONTENT_HASHES, new org.apache.thrift.meta_data.FieldMetaData("contentHashes", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(FetchSourceFilesRequest.class, metaDataMap);
  }

  public FetchSourceFilesRequest() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public FetchSourceFilesRequest(FetchSourceFilesRequest other) {
    if (other.isSetContentHashes()) {
      List<String> __this__contentHashes = new ArrayList<String>(other.contentHashes);
      this.contentHashes = __this__contentHashes;
    }
  }

  public FetchSourceFilesRequest deepCopy() {
    return new FetchSourceFilesRequest(this);
  }

  @Override
  public void clear() {
    this.contentHashes = null;
  }

  public int getContentHashesSize() {
    return (this.contentHashes == null) ? 0 : this.contentHashes.size();
  }

  public java.util.Iterator<String> getContentHashesIterator() {
    return (this.contentHashes == null) ? null : this.contentHashes.iterator();
  }

  public void addToContentHashes(String elem) {
    if (this.contentHashes == null) {
      this.contentHashes = new ArrayList<String>();
    }
    this.contentHashes.add(elem);
  }

  public List<String> getContentHashes() {
    return this.contentHashes;
  }

  public FetchSourceFilesRequest setContentHashes(List<String> contentHashes) {
    this.contentHashes = contentHashes;
    return this;
  }

  public void unsetContentHashes() {
    this.contentHashes = null;
  }

  /** Returns true if field contentHashes is set (has been assigned a value) and false otherwise */
  public boolean isSetContentHashes() {
    return this.contentHashes != null;
  }

  public void setContentHashesIsSet(boolean value) {
    if (!value) {
      this.contentHashes = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CONTENT_HASHES:
      if (value == null) {
        unsetContentHashes();
      } else {
        setContentHashes((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CONTENT_HASHES:
      return getContentHashes();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CONTENT_HASHES:
      return isSetContentHashes();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof FetchSourceFilesRequest)
      return this.equals((FetchSourceFilesRequest)that);
    return false;
  }

  public boolean equals(FetchSourceFilesRequest that) {
    if (that == null)
      return false;

    boolean this_present_contentHashes = true && this.isSetContentHashes();
    boolean that_present_contentHashes = true && that.isSetContentHashes();
    if (this_present_contentHashes || that_present_contentHashes) {
      if (!(this_present_contentHashes && that_present_contentHashes))
        return false;
      if (!this.contentHashes.equals(that.contentHashes))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_contentHashes = true && (isSetContentHashes());
    list.add(present_contentHashes);
    if (present_contentHashes)
      list.add(contentHashes);

    return list.hashCode();
  }

  @Override
  public int compareTo(FetchSourceFilesRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetContentHashes()).compareTo(other.isSetContentHashes());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetContentHashes()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.contentHashes, other.contentHashes);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("FetchSourceFilesRequest(");
    boolean first = true;

    if (isSetContentHashes()) {
      sb.append("contentHashes:");
      if (this.contentHashes == null) {
        sb.append("null");
      } else {
        sb.append(this.contentHashes);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class FetchSourceFilesRequestStandardSchemeFactory implements SchemeFactory {
    public FetchSourceFilesRequestStandardScheme getScheme() {
      return new FetchSourceFilesRequestStandardScheme();
    }
  }

  private static class FetchSourceFilesRequestStandardScheme extends StandardScheme<FetchSourceFilesRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, FetchSourceFilesRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CONTENT_HASHES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list82 = iprot.readListBegin();
                struct.contentHashes = new ArrayList<String>(_list82.size);
                String _elem83;
                for (int _i84 = 0; _i84 < _list82.size; ++_i84)
                {
                  _elem83 = iprot.readString();
                  struct.contentHashes.add(_elem83);
                }
                iprot.readListEnd();
              }
              struct.setContentHashesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, FetchSourceFilesRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.contentHashes != null) {
        if (struct.isSetContentHashes()) {
          oprot.writeFieldBegin(CONTENT_HASHES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.contentHashes.size()));
            for (String _iter85 : struct.contentHashes)
            {
              oprot.writeString(_iter85);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class FetchSourceFilesRequestTupleSchemeFactory implements SchemeFactory {
    public FetchSourceFilesRequestTupleScheme getScheme() {
      return new FetchSourceFilesRequestTupleScheme();
    }
  }

  private static class FetchSourceFilesRequestTupleScheme extends TupleScheme<FetchSourceFilesRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, FetchSourceFilesRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetContentHashes()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetContentHashes()) {
        {
          oprot.writeI32(struct.contentHashes.size());
          for (String _iter86 : struct.contentHashes)
          {
            oprot.writeString(_iter86);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, FetchSourceFilesRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list87 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.contentHashes = new ArrayList<String>(_list87.size);
          String _elem88;
          for (int _i89 = 0; _i89 < _list87.size; ++_i89)
          {
            _elem88 = iprot.readString();
            struct.contentHashes.add(_elem88);
          }
        }
        struct.setContentHashesIsSet(true);
      }
    }
  }

}

