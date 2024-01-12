import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookmarkCreateComponent } from './bookmark-create.component';

describe('BookmarkCreateComponent', () => {
  let component: BookmarkCreateComponent;
  let fixture: ComponentFixture<BookmarkCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookmarkCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookmarkCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
