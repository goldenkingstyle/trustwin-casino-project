/*
 * This file is generated by jOOQ.
 */
package eu.panic.gameminerservice.generatedClasses.tables;


import eu.panic.gameminerservice.generatedClasses.Keys;
import eu.panic.gameminerservice.generatedClasses.Public;
import eu.panic.gameminerservice.generatedClasses.tables.records.SignInHistoryTableRecord;

import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function9;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SignInHistoryTable extends TableImpl<SignInHistoryTableRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.sign_in_history_table</code>
     */
    public static final SignInHistoryTable SIGN_IN_HISTORY_TABLE = new SignInHistoryTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SignInHistoryTableRecord> getRecordType() {
        return SignInHistoryTableRecord.class;
    }

    /**
     * The column <code>public.sign_in_history_table.id</code>.
     */
    public final TableField<SignInHistoryTableRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.sign_in_history_table.browser_name</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> BROWSER_NAME = createField(DSL.name("browser_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.browser_version</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> BROWSER_VERSION = createField(DSL.name("browser_version"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.device_name</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> DEVICE_NAME = createField(DSL.name("device_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.device_type</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> DEVICE_TYPE = createField(DSL.name("device_type"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.operating_system</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> OPERATING_SYSTEM = createField(DSL.name("operating_system"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.ip_address</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> IP_ADDRESS = createField(DSL.name("ip_address"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.sign_in_history_table.timestamp</code>.
     */
    public final TableField<SignInHistoryTableRecord, Long> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.sign_in_history_table.username</code>.
     */
    public final TableField<SignInHistoryTableRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private SignInHistoryTable(Name alias, Table<SignInHistoryTableRecord> aliased) {
        this(alias, aliased, null);
    }

    private SignInHistoryTable(Name alias, Table<SignInHistoryTableRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.sign_in_history_table</code> table
     * reference
     */
    public SignInHistoryTable(String alias) {
        this(DSL.name(alias), SIGN_IN_HISTORY_TABLE);
    }

    /**
     * Create an aliased <code>public.sign_in_history_table</code> table
     * reference
     */
    public SignInHistoryTable(Name alias) {
        this(alias, SIGN_IN_HISTORY_TABLE);
    }

    /**
     * Create a <code>public.sign_in_history_table</code> table reference
     */
    public SignInHistoryTable() {
        this(DSL.name("sign_in_history_table"), null);
    }

    public <O extends Record> SignInHistoryTable(Table<O> child, ForeignKey<O, SignInHistoryTableRecord> key) {
        super(child, key, SIGN_IN_HISTORY_TABLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<SignInHistoryTableRecord, Long> getIdentity() {
        return (Identity<SignInHistoryTableRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<SignInHistoryTableRecord> getPrimaryKey() {
        return Keys.SIGN_IN_HISTORY_TABLE_PKEY;
    }

    @Override
    public SignInHistoryTable as(String alias) {
        return new SignInHistoryTable(DSL.name(alias), this);
    }

    @Override
    public SignInHistoryTable as(Name alias) {
        return new SignInHistoryTable(alias, this);
    }

    @Override
    public SignInHistoryTable as(Table<?> alias) {
        return new SignInHistoryTable(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SignInHistoryTable rename(String name) {
        return new SignInHistoryTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SignInHistoryTable rename(Name name) {
        return new SignInHistoryTable(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SignInHistoryTable rename(Table<?> name) {
        return new SignInHistoryTable(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, String, String, String, String, String, String, Long, String> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function9<? super Long, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Long, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function9<? super Long, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super Long, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
