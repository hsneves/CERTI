import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { PokemonService } from './pokemon.service';
import { of } from 'rxjs';
import { Constants } from '../utils/constants';
import { NgEventBus } from 'ng-event-bus';
import { Expansion } from '@angular/compiler';

describe('PokemonService', () => {

  let service: PokemonService;

  const httpGetStub = {
    get: (path: String) =>
      path == Constants.GET_POKEBALLS ?
      of(
        [
          {
            "id": 1,
            "pokemon": {
              "id": 1,
              "name": "Bulbasaur"
            }
          },
          {
            "id": 2,
            "pokemon": {
              "id": 3,
              "name": "Venusaur"
            }
          },
          {
            "id": 3,
            "pokemon": {
              "id": 4,
              "name": "Charmander"
            }
          },
          {
            "id": 4,
            "pokemon": {
              "id": 25,
              "name": "Pikachu"
            }
          }
        ]
      ) :
      // throw pokeball
      of(
        {
          "id": 4,
          "pokemon": {
            "id": 25,
            "name": "Pikachu"
          }
        }
      )
  }

  const eventBusStub = {

  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: HttpClient,
          useValue: httpGetStub
        },
        {
          provide: NgEventBus,
          useValue: eventBusStub
        }
      ],
      schemas: [
        CUSTOM_ELEMENTS_SCHEMA
      ],
    });
    service = TestBed.inject(PokemonService);
  });

  it('deve ser criado', () => {
    expect(service).toBeTruthy();
  });

  it('deve chamar um GET com o endpoint correto ao recuperar as pokebolas', () => {
    const spy = spyOn((service as any).http as HttpClient, "get").and.callThrough();
    service.getPokeballs();
    expect(spy).toHaveBeenCalledWith(Constants.GET_POKEBALLS);
  });

  it('deve recuperar as pokebolas', () => {
    service.getPokeballs().subscribe(pokeballs => {
      expect(pokeballs.length).toEqual(4);
      expect(pokeballs[0].pokemon.name).toEqual("Bulbasaur");
      expect(pokeballs[1].pokemon.id).toEqual(3);
      expect(pokeballs[2].pokemon.name).toEqual("Charmander");
    });
  });

  it('deve chamar um GET com o endpoint correto ao lançar uma pokebola', () => {
    var pokemon = "pikachu";
    const spy = spyOn((service as any).http as HttpClient, "get").and.callThrough();
    service.throwPokeball(pokemon).subscribe(pokeball => {});
    expect(spy).toHaveBeenCalledWith(Constants.THROW_POKEBALL + pokemon);
  });

  it('deve capturar o pokemon pikachu', () => {
    var pokemon = "pikachu";
    const spy = spyOn((service as any).http as HttpClient, "get").and.callThrough();
    service.throwPokeball(pokemon).subscribe(pokeball => {
      expect(pokeball.pokemon).toBeDefined();
      expect(pokeball.pokemon.name).toEqual("Pikachu");
    });
  });

});