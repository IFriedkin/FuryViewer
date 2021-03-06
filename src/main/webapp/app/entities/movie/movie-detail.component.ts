import {Component, OnInit, OnDestroy, ViewChild, ElementRef, HostListener} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Movie } from './movie.model';
import { MovieService } from './movie.service';
import {NgbRatingConfig} from '@ng-bootstrap/ng-bootstrap';
import {factoryOrValue} from 'rxjs/operator/multicast';
import {FavouriteMovieService} from '../favourite-movie/favourite-movie.service';
import {Observable} from 'rxjs/Observable';
import {FavouriteMovie} from '../favourite-movie/favourite-movie.model';
import {Social} from "../social/social.model";
import {ResponseWrapper} from "../../shared/model/response-wrapper.model";
import {SocialService} from "../social/social.service";
import {HatredMovieService} from "../hatred-movie/hatred-movie.service";
import {HatredMovie} from "../hatred-movie/hatred-movie.model";
import {ArtistService} from "../artist/artist.service";
import {Artist} from "../artist/artist.model";
import {RateMovie} from "../rate-movie/rate-movie.model";
import {RateMovieService} from "../rate-movie/rate-movie.service";

@Component({
    selector: 'jhi-movie-detail',
    templateUrl: './movie-detail.component.html',
    providers: [NgbRatingConfig],
    styles: [`
    .star {
      font-size: 1.5rem;
      color: #b0c4de;
    }
    .filled {
      color: #1e90ff;
    }
    .bad {
      color: #deb0b0;
    }
    .filled.bad {
      color: #ff1e1e;
    }
  `]
})
export class MovieDetailComponent implements OnInit, OnDestroy {
    movie: Movie;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    fav: FavouriteMovie;
    marks: Social[];
    hate: HatredMovie;
    artistMovie: Artist[];
    rateUser: RateMovie;
    media: string;

    constructor(
        private eventManager: JhiEventManager,
        private movieService: MovieService,
        private route: ActivatedRoute,
        config: NgbRatingConfig,
        private favouriteMovieService: FavouriteMovieService,
        private socialService: SocialService,
        private hatredMovieService: HatredMovieService,
        private jhiAlertService: JhiAlertService,
        private artistService: ArtistService,
        private rateMovieServie: RateMovieService
    ) {
        config.max = 5;
        this.fav = new FavouriteMovie();
        this.fav.liked=false;
        this.hate = new HatredMovie();
        this.hate.hated=false;
        this.rateUser = new RateMovie()
        this.rateUser.rate = 0;
        this.media = "0";
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
            this.getIfLiked(params['id']);
            this.loadMarks(params['id']);
            this.getIfHatred(params['id']);
            this.loadArtist(params['id']);
            this.loadRateUser(params['id']);
            this.loadMediumMark(params['id']);
        });
        this.registerChangeInMovies();

    }

    load(id) {
        this.movieService.find(id).subscribe((movie) => {
            this.movie = movie;
        });
    }
    loadArtist(id){
        this.artistService.findMovieActors(id).subscribe(
            (res: ResponseWrapper) => {
                this.artistMovie = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadRateUser(id: number){
        this.rateMovieServie.markMovieUser(id).subscribe((rateMovie) => {
            this.rateUser = rateMovie;
        });
    }

    loadMediumMark(id: number){
        this.rateMovieServie.mediaMovie(id).subscribe((rateMovie) => {
            let ourMark = rateMovie.toPrecision(2);
            this.media = ourMark;



        });
    }
    previousState() {
        window.history.back();
    }

    like() {
        this.subscribeToLikeResponse(
            this.favouriteMovieService.favorite(this.movie.id)
        );


    }

    hated(){
        this.subscribeToHateResponse(
            this.hatredMovieService.hate(this.movie.id)
        );
    }

    rate(){
        this.subscribeToSaveResponseRate(
            this.rateMovieServie.Rate(this.movie.id, this.rateUser.rate)
        );
    }

    loadMarks(id:number){
        this.socialService.movieMarks(id).subscribe(
            (res: ResponseWrapper) => {
                this.marks = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    getIfLiked(id: number) {
        this.favouriteMovieService.favMovieUser(id).subscribe((favouriteMovie) => {
            console.log('load');
            this.fav = favouriteMovie;
        });
    }

    getIfHatred(id: number){
        this.hatredMovieService.getIfHatred(id).subscribe((hatredMovie) => {
            console.log('load');
            this.hate = hatredMovie;
        });
    }

    private subscribeToLikeResponse(result: Observable<FavouriteMovie>) {
        result.subscribe((res: FavouriteMovie) =>
            this.onSaveSuccessLike(res), (res: Response) => this.onSaveErrorLike());
    }
    private onSaveSuccessLike(result: FavouriteMovie) {
        this.eventManager.broadcast({ name: 'favouriteMovieListModification', content: 'OK'});
        this.fav=result;
        if(this.hate.hated && this.fav.liked){
            this.subscribeToHateResponse(
                this.hatredMovieService.hate(this.movie.id)
            );

        }
    }

    private subscribeToHateResponse(result: Observable<HatredMovie>) {
        result.subscribe((res: HatredMovie) =>
            this.onSaveSuccessHate(res), (res: Response) => this.onSaveErrorLike());
    }
    private onSaveSuccessHate(result: HatredMovie) {
        this.eventManager.broadcast({ name: 'favouriteMovieListModification', content: 'OK'});
        this.hate=result;
        if(this.hate.hated && this.fav.liked){
            this.subscribeToLikeResponse(
                this.favouriteMovieService.favorite(this.movie.id)
            );
        }
    }

    private subscribeToSaveResponseRate(result: Observable<RateMovie>) {
        result.subscribe((res: RateMovie) =>
            this.onSaveSuccessRate(res), (res: Response) => this.onSaveErrorLike());
    }

    private onSaveSuccessRate(result: RateMovie) {
        this.eventManager.broadcast({ name: 'rateMovieListModification', content: 'OK'});
        this.rateUser=result;
        this.loadMediumMark(this.movie.id);
    }



    private onSaveErrorLike() {

    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMovies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'movieListModification',
            (response) => this.load(this.movie.id)
        );
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

}
