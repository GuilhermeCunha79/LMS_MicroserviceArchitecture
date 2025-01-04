--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2 (Debian 17.2-1.pgdg120+1)
-- Dumped by pg_dump version 17.2 (Debian 17.2-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: forbidden_name; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.forbidden_name (
                                       pk bigint NOT NULL,
                                       forbidden_name character varying(255) NOT NULL
);


ALTER TABLE public.forbidden_name OWNER TO postgres;

--
-- Name: forbidden_name_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.forbidden_name_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.forbidden_name_seq OWNER TO postgres;

--
-- Name: photo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.photo (
                              pk bigint NOT NULL,
                              photo_file character varying(255) NOT NULL
);


ALTER TABLE public.photo OWNER TO postgres;

--
-- Name: photo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.photo_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.photo_seq OWNER TO postgres;

--
-- Name: t_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user (
                               dtype character varying(31) NOT NULL,
                               user_id bigint NOT NULL,
                               created_at timestamp(6) without time zone NOT NULL,
                               created_by character varying(255) NOT NULL,
                               enabled boolean NOT NULL,
                               modified_at timestamp(6) without time zone NOT NULL,
                               modified_by character varying(255) NOT NULL,
                               name character varying(150),
                               password character varying(255) NOT NULL,
                               username character varying(255) NOT NULL,
                               version bigint NOT NULL
);


ALTER TABLE public.t_user OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
                                   user_id bigint NOT NULL,
                                   role bytea
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: forbidden_name forbidden_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.forbidden_name
    ADD CONSTRAINT forbidden_name_pkey PRIMARY KEY (pk);


--
-- Name: photo photo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (pk);


--
-- Name: t_user t_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT t_user_pkey PRIMARY KEY (user_id);


--
-- Name: t_user uk_jhib4legehrm4yscx9t3lirqi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT uk_jhib4legehrm4yscx9t3lirqi UNIQUE (username);


--
-- Name: user_roles fkn3jfyu68eps3hj1bgy577lrfs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkn3jfyu68eps3hj1bgy577lrfs FOREIGN KEY (user_id) REFERENCES public.t_user(user_id);


--
-- PostgreSQL database dump complete
--