import { TestBed } from '@angular/core/testing';

import { PokemonCaughtSseService } from './pokemon-caught-sse.service';

describe('PokemonCaughtSseService', () => {
  let service: PokemonCaughtSseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PokemonCaughtSseService);
  });

  it('deve criar o componente', () => {
    expect(service).toBeTruthy();
  });
});
