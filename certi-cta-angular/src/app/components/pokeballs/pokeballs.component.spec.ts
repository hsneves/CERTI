import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MetaData, NgEventBus } from 'ng-event-bus';
import { Observable, of } from 'rxjs';
import { Constants } from 'src/app/utils/constants';
import { HttpClient } from '@angular/common/http';
import { PokeballsComponent } from './pokeballs.component';
import { FormBuilder } from "@angular/forms";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Pokeball } from 'src/app/models/pokeball';
import { By } from '@angular/platform-browser';

describe('PokeballsComponent', () => {
  let component: PokeballsComponent;
  let fixture: ComponentFixture<PokeballsComponent>;

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
    on: (param: String) => {
      return new Observable<MetaData>();
    },
    subscribe: (meta: MetaData) => {
      return {data:
        {
          "id": 4,
          "pokemon": {
            "id": 25,
            "name": "Pikachu"
          }
        }};
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {
          provide: HttpClient,
          useValue: httpGetStub
        },
        {
          provide: NgEventBus,
          useValue: eventBusStub
        },
        FormBuilder
      ],
      imports: [
        FormsModule,
        ReactiveFormsModule
      ],
      declarations: [PokeballsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PokeballsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve ser criado', () => {
    expect(component).toBeTruthy();
  });

  it('deve recuperar as pokebolas', () => {
    component.loadPokeballs();
    expect(component.pokeballs).toBeDefined();
    expect(component.pokeballs.length).toEqual(4);
  });

  it('deve retornar a URL correta da imagem do pokemon', () => {
    var url = component.getPokemonImageUrl({ id: 1, pokemon: { id: 25, name: "Pikachu" } });
    expect(url).toEqual(Constants.URL_POKEMON_IMAGE + 25 + ".png");
  });

  it('deve retornar a URL correta da pokebola vazia', () => {
    var pokeballEmpty = new Object({ id: 1 });
    var url = component.getPokemonImageUrl(pokeballEmpty as Pokeball);
    expect(url).toEqual(Constants.URL_EMPTY_POKEBALL);
  });

  it('teste capturar pokemon', () => {

    const pokemonNameTextInput = ((fixture.debugElement.query(By.css("#pokemonName"))) as unknown as HTMLInputElement);
    pokemonNameTextInput.value = "pikaChU";
       
    const formThrowPokeball = fixture.debugElement.query(By.css("#formThrowPokeball"));
    const fnc = spyOn(component, "throwPokeball");

    formThrowPokeball.triggerEventHandler("ngSubmit", null);

    expect(fnc).toHaveBeenCalled();

    fixture.detectChanges();

    // campo com as informações do pokemon capturado deve estar visível
    const pokeballWithPokemon = ((fixture.debugElement.query(By.css("#pokeballWithPokemon"))) as unknown as Element);

    expect(component.pokeball).toBeUndefined();
  });

});
