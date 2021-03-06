import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import { withTranslation } from 'react-i18next';
import Spinner from 'react-bootstrap/Spinner';
import GameService from "../../../services/api/gameService";
import Pagination from "../../common/Pagination/Pagination";
import withQuery from '../../hoc/withQuery';
import withRedirect from '../../hoc/withRedirect';
import GamesCard from "../../common/GamesCard/GamesCard";
import SearchModal from "../../common/SearchModal/SearchModal"
import {CREATED, OK} from "../../../services/api/apiConstants";
import ErrorContent from "../../common/ErrorContent/ErrorContent";

class SearchGameResults extends Component {
    state = {
        path : window.location.pathname.substring(1 + (`${process.env.PUBLIC_URL}`).length),
        pagination: [],
        content : [],
        data : null,
        page : null,
        searchParams: {},
        pageCount : null,
        loading : true,
        error : false,
        status : null,
    };

    componentWillMount() {
        this.setPage(this.props);
    }

    componentWillReceiveProps(newProps) {
        this.setPage(newProps);
    }

    setPage(props) {
        if(!props)
            props = this.props;
        let page = props.query.get("page");
        let searchParams = {hoursLeft: props.query.get("hoursLeft"), minsLeft: props.query.get("minsLeft"), secsLeft: props.query.get("secsLeft"),
                            hoursRight: props.query.get("hoursRight"), minsRight: props.query.get("minsRight"), secsRight: props.query.get("secsRight"),
                            scoreLeft: props.query.get("scoreLeft"), scoreRight: props.query.get("scoreRight"),
                            platforms: props.query.getAll("platforms"), genres: props.query.getAll("genres"), searchTerm: props.query.get("searchTerm")}
        if(!page) {
            page = 1;
        }
        if(!searchParams.searchTerm) {
            searchParams.searchTerm = '';
        }
        GameService.searchGamesPage(searchParams, page).then((response) => {
            let findError = null;
            if (response.status && response.status !== OK && response.status !== CREATED) {
                findError = response.status;
            }
            if(findError) {
                this.setState({
                    loading: false,
                    error: true,
                    status: findError,
                });
            }
            else {
                this.setState({
                    loading: false,
                    content: response.content,
                    pagination: response.pagination,
                    pageCount: response.pageCount,
                    totalCount: response.totalCount,
                    page: page,
                    searchParams: searchParams,
                });
                if (response.totalCount === "1") {
                    this.props.addRedirection("gameProfile", `/games/${response.content[0].id}`);
                    this.props.activateRedirect("gameProfile");
                }
            }
        });
    }

    render() {
        const { t } = this.props
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        if(this.state.error) {
            return <ErrorContent status={this.state.status}/>
        }
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t("search.helmet", {value: this.state.searchParams.searchTerm})} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <SearchModal searchParams={this.state.searchParams} path={this.state.path}/>
                <GamesCard label={"search.gameResults"} labelArgs={this.state.searchParams.searchTerm} items={this.state.content} search={true} totalCount={this.state.totalCount}/>
                <Pagination url={this.state.path} page={this.state.page} totalPages={this.state.pageCount} setPage={this.setPage} queryParams={this.state.searchParams}/>
            </React.Fragment>
        );
    }
}
export default withTranslation() (withQuery(withRedirect(SearchGameResults)));
