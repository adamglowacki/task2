--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: announcement_instance; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE announcement_instance (
    id bigint NOT NULL,
    readdate timestamp without time zone,
    readstatus boolean NOT NULL,
    announcement_id bigint NOT NULL,
    receiver_id bigint NOT NULL
);


ALTER TABLE public.announcement_instance OWNER TO hibernate;

--
-- Name: classace; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE classace (
    id bigint NOT NULL,
    canonicaltypename character varying(255) NOT NULL,
    rightstype integer NOT NULL,
    user_id bigint
);


ALTER TABLE public.classace OWNER TO hibernate;

--
-- Name: content; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE content (
    contenttype character varying(31) NOT NULL,
    id bigint NOT NULL,
    body character varying(255),
    title character varying(255) NOT NULL
);


ALTER TABLE public.content OWNER TO hibernate;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: hibernate
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO hibernate;

--
-- Name: instanceace; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE instanceace (
    id bigint NOT NULL,
    objecthashcode bigint NOT NULL,
    rightstype integer NOT NULL,
    user_id bigint
);


ALTER TABLE public.instanceace OWNER TO hibernate;

--
-- Name: secgroup; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE secgroup (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.secgroup OWNER TO hibernate;

--
-- Name: secuser; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE secuser (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    surname character varying(255) NOT NULL
);


ALTER TABLE public.secuser OWNER TO hibernate;

--
-- Name: secuser_secgroup; Type: TABLE; Schema: public; Owner: hibernate; Tablespace: 
--

CREATE TABLE secuser_secgroup (
    members_id bigint NOT NULL,
    groups_id bigint NOT NULL
);


ALTER TABLE public.secuser_secgroup OWNER TO hibernate;

--
-- Name: announcement_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY announcement_instance
    ADD CONSTRAINT announcement_instance_pkey PRIMARY KEY (id);


--
-- Name: classace_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY classace
    ADD CONSTRAINT classace_pkey PRIMARY KEY (id);


--
-- Name: classace_user_id_canonicaltypename_key; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY classace
    ADD CONSTRAINT classace_user_id_canonicaltypename_key UNIQUE (user_id, canonicaltypename);


--
-- Name: content_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY content
    ADD CONSTRAINT content_pkey PRIMARY KEY (id);


--
-- Name: content_title_key; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY content
    ADD CONSTRAINT content_title_key UNIQUE (title);


--
-- Name: instanceace_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY instanceace
    ADD CONSTRAINT instanceace_pkey PRIMARY KEY (id);


--
-- Name: instanceace_user_id_objecthashcode_key; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY instanceace
    ADD CONSTRAINT instanceace_user_id_objecthashcode_key UNIQUE (user_id, objecthashcode);


--
-- Name: secgroup_name_key; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY secgroup
    ADD CONSTRAINT secgroup_name_key UNIQUE (name);


--
-- Name: secgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY secgroup
    ADD CONSTRAINT secgroup_pkey PRIMARY KEY (id);


--
-- Name: secuser_name_surname_key; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY secuser
    ADD CONSTRAINT secuser_name_surname_key UNIQUE (name, surname);


--
-- Name: secuser_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY secuser
    ADD CONSTRAINT secuser_pkey PRIMARY KEY (id);


--
-- Name: secuser_secgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: hibernate; Tablespace: 
--

ALTER TABLE ONLY secuser_secgroup
    ADD CONSTRAINT secuser_secgroup_pkey PRIMARY KEY (members_id, groups_id);


--
-- Name: fk5bad2e1124816e5; Type: FK CONSTRAINT; Schema: public; Owner: hibernate
--

ALTER TABLE ONLY secuser_secgroup
    ADD CONSTRAINT fk5bad2e1124816e5 FOREIGN KEY (members_id) REFERENCES secuser(id);


--
-- Name: fk5bad2e119a0f2bec; Type: FK CONSTRAINT; Schema: public; Owner: hibernate
--

ALTER TABLE ONLY secuser_secgroup
    ADD CONSTRAINT fk5bad2e119a0f2bec FOREIGN KEY (groups_id) REFERENCES secgroup(id);


--
-- Name: fkf788788d88ba45a9; Type: FK CONSTRAINT; Schema: public; Owner: hibernate
--

ALTER TABLE ONLY announcement_instance
    ADD CONSTRAINT fkf788788d88ba45a9 FOREIGN KEY (announcement_id) REFERENCES content(id);


--
-- Name: fkf788788dd4d83e4f; Type: FK CONSTRAINT; Schema: public; Owner: hibernate
--

ALTER TABLE ONLY announcement_instance
    ADD CONSTRAINT fkf788788dd4d83e4f FOREIGN KEY (receiver_id) REFERENCES secuser(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

