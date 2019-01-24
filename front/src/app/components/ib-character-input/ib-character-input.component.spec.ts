import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbCharacterInputComponent } from './ib-character-input.component';

describe('IbCharacterInputComponent', () => {
  let component: IbCharacterInputComponent;
  let fixture: ComponentFixture<IbCharacterInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbCharacterInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbCharacterInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
