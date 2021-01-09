import React, { Component } from 'react';
import { Helmet, HelmetProvider } from 'react-helmet-async';
import GameService from "../../../services/api/gameService";
import Spinner from 'react-bootstrap/Spinner';
import ItemsCard from "../../common/ItemsCard/ItemsCard";


class IndexPage extends Component {
    state = {
        popularGames : [],
        upcomingGames : [],
        loading : true,
    };

    componentWillMount() {
        const fetchPop = GameService.getPopularGames();
        const fetchUp = GameService.getUpcomingGames();

        //TODO: Handle no response (404)
        Promise.all([ fetchPop, fetchUp ]).then((responses) => {
            this.setState({
                loading: false,
                popularGames: responses[0],
                upcomingGames : responses[1],
            });
        });
        console.log(this.state.upcomingGames);
    }

    render() {
        if (this.state.loading === true) {
            return <Spinner animation="border" variant="primary" />;
        }
        return (  
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <div>
                    <ItemsCard items={this.state.popularGames} label={"games.lists.popularGames"}/>
                </div>
                <div>
                    <ItemsCard items={this.state.upcomingGames} label={"games.lists.upcomingGames"}/>
                </div>
            </React.Fragment>
        );
    }
}
 
export default IndexPage;