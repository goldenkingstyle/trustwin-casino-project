/*
 * This file is generated by jOOQ.
 */
package eu.panic.gamejackpotservice.generatedClasses.tables;


import eu.panic.gamejackpotservice.generatedClasses.Keys;
import eu.panic.gamejackpotservice.generatedClasses.Public;
import eu.panic.gamejackpotservice.generatedClasses.tables.records.MessagesTableRecord;

import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
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
public class MessagesTable extends TableImpl<MessagesTableRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.messages_table</code>
     */
    public static final MessagesTable MESSAGES_TABLE = new MessagesTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MessagesTableRecord> getRecordType() {
        return MessagesTableRecord.class;
    }

    /**
     * The column <code>public.messages_table.id</code>.
     */
    public final TableField<MessagesTableRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.messages_table.type</code>.
     */
    public final TableField<MessagesTableRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.messages_table.username</code>.
     */
    public final TableField<MessagesTableRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.messages_table.message</code>.
     */
    public final TableField<MessagesTableRecord, String> MESSAGE = createField(DSL.name("message"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.messages_table.timestamp</code>.
     */
    public final TableField<MessagesTableRecord, Long> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.BIGINT.nullable(false), this, "");

    private MessagesTable(Name alias, Table<MessagesTableRecord> aliased) {
        this(alias, aliased, null);
    }

    private MessagesTable(Name alias, Table<MessagesTableRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.messages_table</code> table reference
     */
    public MessagesTable(String alias) {
        this(DSL.name(alias), MESSAGES_TABLE);
    }

    /**
     * Create an aliased <code>public.messages_table</code> table reference
     */
    public MessagesTable(Name alias) {
        this(alias, MESSAGES_TABLE);
    }

    /**
     * Create a <code>public.messages_table</code> table reference
     */
    public MessagesTable() {
        this(DSL.name("messages_table"), null);
    }

    public <O extends Record> MessagesTable(Table<O> child, ForeignKey<O, MessagesTableRecord> key) {
        super(child, key, MESSAGES_TABLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<MessagesTableRecord, Integer> getIdentity() {
        return (Identity<MessagesTableRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<MessagesTableRecord> getPrimaryKey() {
        return Keys.MESSAGES_TABLE_PKEY;
    }

    @Override
    public MessagesTable as(String alias) {
        return new MessagesTable(DSL.name(alias), this);
    }

    @Override
    public MessagesTable as(Name alias) {
        return new MessagesTable(alias, this);
    }

    @Override
    public MessagesTable as(Table<?> alias) {
        return new MessagesTable(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public MessagesTable rename(String name) {
        return new MessagesTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MessagesTable rename(Name name) {
        return new MessagesTable(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public MessagesTable rename(Table<?> name) {
        return new MessagesTable(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, String, String, String, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Integer, ? super String, ? super String, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Integer, ? super String, ? super String, ? super String, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
